package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.interfaces.APIEndpoint;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Rarity;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RarityEndpoint extends APIEndpoint<Rarity, Integer> {

    public RarityEndpoint(String apiRoot) {

        super(apiRoot);
    }

    public static Rarity getDefaultRarity() {

        return new Rarity(0, "Unknown", Color.BLACK);
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
            JSONArray   array   = request.readJsonArray();

            for (int i = 0 ; i < array.length() ; i++) {
                JSONObject       o      = array.getJSONObject(i);
                Optional<Rarity> rarity = Rarity.from(o);
                rarity.ifPresent(rarityList::add);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rarityList;
    }

}
