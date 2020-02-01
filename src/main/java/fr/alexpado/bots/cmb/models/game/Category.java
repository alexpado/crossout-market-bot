package fr.alexpado.bots.cmb.models.game;

import fr.alexpado.bots.cmb.interfaces.JSONModel;
import lombok.Getter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Optional;

@Getter
public class Category extends JSONModel {

    private int id;
    private String name;

    public Category(JSONObject dataSource) throws Exception {
        super(dataSource);
    }

    public Category(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Optional<Category> from(JSONObject dataSource) {
        try {
            return Optional.of(new Category(dataSource));
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

}
