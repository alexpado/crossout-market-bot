package fr.alexpado.bots.cmb.cleaning.xodb.category;

import fr.alexpado.bots.cmb.cleaning.XoDB;
import fr.alexpado.bots.cmb.cleaning.entities.game.Category;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.ICategory;
import fr.alexpado.bots.cmb.cleaning.rest.method.GetRestRequest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FindAllCategoriesAction extends GetRestRequest<List<ICategory>> {

    private final XoDB xoDB;

    public FindAllCategoriesAction(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    /**
     * Retrieve the string version that will be used to send the request.
     *
     * @return An URL as String
     */
    @Override
    public String getTargetUrl() {

        return String.format("%s/categories", this.xoDB.getRootUrl());
    }

    /**
     * Convert the response's body to the desired type.
     *
     * @param output
     *         The response's body.
     *
     * @return An object of desired type.
     */
    @Override
    public List<ICategory> convert(String output) {

        JSONArray       array      = new JSONArray(output);
        List<ICategory> categories = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            categories.add(new Category(array.getJSONObject(i)));
        }

        return categories;
    }
}
