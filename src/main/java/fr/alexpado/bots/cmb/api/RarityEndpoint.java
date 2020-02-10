package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.interfaces.APIEndpoint;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.models.game.Rarity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.*;

public class RarityEndpoint extends APIEndpoint<Rarity, Integer> {

    private Map<Integer, Color> colorMap = new HashMap<>();

    public RarityEndpoint(String apiRoot) {
        super(apiRoot);
        colorMap.put(1, new Color(151, 151, 151));
        colorMap.put(2, new Color(0, 64, 253));
        colorMap.put(3, new Color(106, 0, 130));
        colorMap.put(4, new Color(218, 165, 32));
        colorMap.put(5, new Color(210, 151, 30));
    }

    @Override
    public Optional<Rarity> getOne(Integer id) {
        List<Rarity> rarities = this.getAll();
        for (Rarity rarity : rarities) {
            if (rarity.getId() == id) {
                return Optional.of(rarity);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Rarity> getAll() {
        List<Rarity> rarityList = new ArrayList<>();

        try {
            HttpRequest request = new HttpRequest(String.format("%s/rarities", this.getHost()));
            JSONArray array = request.readJsonArray();

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                o.put("color", this.getColorFor(o.getInt("id")).getRGB());
                Optional<Rarity> rarity = Rarity.from(o);
                rarity.ifPresent(rarityList::add);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rarityList;
    }

    public Color getColorFor(int id) {
        return colorMap.get(id);
    }
}
