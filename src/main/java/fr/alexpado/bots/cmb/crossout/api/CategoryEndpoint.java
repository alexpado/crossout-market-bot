package fr.alexpado.bots.cmb.crossout.api;

import fr.alexpado.bots.cmb.crossout.models.game.Category;
import fr.alexpado.bots.cmb.interfaces.APIRepository;
import fr.alexpado.bots.cmb.libs.HttpRequest;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryEndpoint extends APIRepository<Category> {

    public CategoryEndpoint(String apiRoot) {
        super(apiRoot);
    }

    @Override
    public Optional<Category> getOne(int id) {
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
            HttpRequest request = new HttpRequest(String.format("%s/categories", this.getApiRoot()));
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
}
