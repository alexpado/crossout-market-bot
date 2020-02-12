package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.interfaces.APIEndpoint;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.models.game.Category;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryEndpoint extends APIEndpoint<Category, Integer> {

    public CategoryEndpoint(String apiRoot) {
        super(apiRoot);
    }

    @Override
    public Optional<Category> getOne(Integer id) {
        List<Category> categories = this.getAll();
        for (Category type : categories) {
            if (type.getId() == id) {
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Category> getAll() {
        List<Category> categoryList = new ArrayList<>();

        try {
            HttpRequest request = new HttpRequest(String.format("%s/categories", this.getHost()));
            JSONArray array = request.readJsonArray();

            for (int i = 0; i < array.length(); i++) {
                JSONObject o = array.getJSONObject(i);
                Optional<Category> category = Category.from(o);
                category.ifPresent(categoryList::add);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return categoryList;
    }

    public boolean exists(String name) {


        for (Category element : this.getAll()) {
            if (element.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
