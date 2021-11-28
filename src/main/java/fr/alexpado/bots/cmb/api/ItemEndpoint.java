package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.interfaces.APIEndpoint;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Item;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Rarity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class ItemEndpoint extends APIEndpoint<Item, Integer> {

    private final CrossoutConfiguration config;
    private final RarityEndpoint        rarityEndpoint;

    public ItemEndpoint(CrossoutConfiguration config) {

        super(config.getApiHost());
        this.config         = config;
        this.rarityEndpoint = new RarityEndpoint(config.getApiHost());
    }

    private List<Item> readResponse(JSONArray array) {

        HashMap<Integer, Rarity> rarities = new HashMap<>();

        this.rarityEndpoint.getAll().forEach(rarity -> rarities.put(rarity.getId(), rarity));

        List<Item> itemList = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            JSONObject     o    = array.getJSONObject(i);
            Optional<Item> item = Item.from(this.config, o, rarities);
            item.ifPresent(itemList::add);
        }

        // 2021-11-28 | Anti dupe name fix
        Map<String, Item> nameMap = new HashMap<>();

        for (Item item : itemList) {
            if (nameMap.containsKey(item.getAvailableName())) {
                nameMap.get(item.getAvailableName()).markAsDupe();
                item.markAsDupe();
            } else {
                nameMap.put(item.getAvailableName(), item);
            }
        }

        return itemList;
    }

    @Override
    public Optional<Item> getOne(Integer id) {

        return this.getOne(id, this.config.getDefaultLocale());
    }

    public Optional<Item> getOne(Integer id, String language) {

        try {
            HttpRequest request = new HttpRequest(String.format("%s/item/%s?removedItems=true&metaItems=true&language=%s", this.getHost(), id, language));
            JSONArray array = request.readJsonArray();
            if (array.length() == 1) {
                return Item.from(this.config, array.getJSONObject(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Item> getAll() {

        return this.getAll(this.config.getDefaultLocale());
    }

    public List<Item> getAll(String language) {

        try {
            HttpRequest request = new HttpRequest(String.format("%s/items?language=%s", this.getHost(), language));
            JSONArray   array   = request.readJsonArray();
            return this.readResponse(array);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Item> searchByName(String itemName, String language) {

        HashMap<String, String> query = new HashMap<>();
        query.put("query", itemName);
        query.put("language", language);
        return this.search(query);
    }

    public List<Item> search(Map<String, String> params) {

        List<String> allowedParams  = Arrays.asList("rarity", "category", "faction", "removedItems", "metaItems", "query", "language");
        List<String> filteredParams = new ArrayList<>();

        params.forEach((k, v) -> {
            if (allowedParams.contains(k)) {
                filteredParams.add(k + "=" + URLEncoder.encode(v.toLowerCase(), StandardCharsets.UTF_8));
            }
        });

        try {
            HttpRequest request = new HttpRequest(String.format("%s/items?%s", this.getHost(), String.join("&", filteredParams)));
            JSONArray   array   = request.readJsonArray();
            return this.readResponse(array);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
