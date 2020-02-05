package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.interfaces.APIRepository;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.models.game.Item;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

public class ItemEndpoint extends APIRepository<Item> {

    public ItemEndpoint(String apiRoot) {
        super(apiRoot);
    }

    private List<Item> readResponse(JSONArray array) {
        List<Item> itemList = new ArrayList<>();

        for (int i = 0; i < array.length(); i++) {
            JSONObject o = array.getJSONObject(i);
            Optional<Item> item = Item.from(o);
            item.ifPresent(itemList::add);
        }

        return itemList;
    }

    @Override
    public Optional<Item> getOne(int id) {
        try {
            HttpRequest request = new HttpRequest(String.format("%s/item/%s?removedItems=true&metaItems=true", this.getApiRoot(), id));
            JSONArray array = request.readJsonArray();
            if (array.length() == 1) {
                return Item.from(array.getJSONObject(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Item> getAll() {
        try {
            HttpRequest request = new HttpRequest(String.format("%s/items", this.getApiRoot()));
            JSONArray array = request.readJsonArray();
            return this.readResponse(array);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Item> search(Map<String, String> params) {

        List<String> allowedParams = Arrays.asList("rarity", "category", "faction", "removedItems", "metaItems", "query");
        List<String> filteredParams = new ArrayList<>();

        params.forEach((k, v) -> {
            if (allowedParams.contains(k.toLowerCase())) {
                try {
                    filteredParams.add(k.toLowerCase() + "=" + URLEncoder.encode(v.toLowerCase(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        });

        try {
            HttpRequest request = new HttpRequest(String.format("%s/items?%s", this.getApiRoot(), String.join("&", filteredParams)));
            JSONArray array = request.readJsonArray();
            return this.readResponse(array);
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

}
