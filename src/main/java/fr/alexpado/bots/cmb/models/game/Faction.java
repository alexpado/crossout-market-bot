package fr.alexpado.bots.cmb.models.game;

import fr.alexpado.bots.cmb.interfaces.JSONModel;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

@Getter
public class Faction extends JSONModel {

    private int id;
    private String name;

    public Faction(JSONObject dataSource) throws Exception {
        super(dataSource);
    }

    public Faction(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Optional<Faction> from(JSONObject dataSource) {
        try {
            return Optional.of(new Faction(dataSource));
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

            return true;
        } catch (JSONException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
