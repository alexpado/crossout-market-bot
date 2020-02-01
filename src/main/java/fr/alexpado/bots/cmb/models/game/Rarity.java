package fr.alexpado.bots.cmb.models.game;

import fr.alexpado.bots.cmb.interfaces.JSONModel;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.Optional;

@Getter
public class Rarity extends JSONModel {

    private int id;
    private String name;
    private Color color;

    public Rarity(JSONObject dataSource) throws Exception {
        super(dataSource);
    }

    public Rarity(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Optional<Rarity> from(JSONObject dataSource) {
        try {
            return Optional.of(new Rarity(dataSource));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean reload(JSONObject dataSource) {
        try {
            this.id = dataSource.getInt("id");
            this.name = dataSource.getString("name");
            this.color = new Color(dataSource.getInt("color"));

            return true;
        } catch (JSONException e) {
            return false;
        }
    }

}
