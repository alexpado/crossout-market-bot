package fr.alexpado.bots.cmb.modules.crossout.models.game;

import fr.alexpado.bots.cmb.interfaces.JSONModel;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.Optional;

@Getter
public class Rarity extends JSONModel {

    private int    id;
    private String name;
    private Color  color;

    public Rarity(JSONObject dataSource) throws Exception {

        super(dataSource);
    }

    public Rarity(int id, String name) {

        this.id   = id;
        this.name = name;
    }

    public Rarity(int id, String name, Color color) {

        this(id, name);
        this.color = color;
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
            this.id   = dataSource.getInt("id");
            this.name = dataSource.getString("name");

            String colorHex = dataSource.getString("primarycolor");
            int    r        = Integer.parseInt(colorHex.substring(0, 2), 16);
            int    g        = Integer.parseInt(colorHex.substring(2, 4), 16);
            int    b        = Integer.parseInt(colorHex.substring(4, 6), 16);
            this.color = new Color(r, g, b);

            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    @Override
    public String toString() {

        return this.name;
    }

}
