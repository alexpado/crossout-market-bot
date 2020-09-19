package xo.marketbot.xodb.repositories.category;

import fr.alexpado.lib.rest.RestAction;
import fr.alexpado.lib.rest.enums.RequestMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import xo.marketbot.entities.game.Category;
import xo.marketbot.interfaces.game.ICategory;
import xo.marketbot.xodb.XoDB;

import java.util.ArrayList;
import java.util.List;

public class FindAllCategoriesAction extends RestAction<List<ICategory>> {

    private final XoDB xoDB;

    public FindAllCategoriesAction(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    @Override
    public @NotNull RequestMethod getRequestMethod() {

        return RequestMethod.GET;
    }

    @Override
    public @NotNull String getRequestURL() {

        return String.format("%s/categories", this.xoDB.getRootUrl());
    }

    @Override
    public List<ICategory> convert(byte[] requestBody) {

        JSONArray       array      = new JSONArray(new String(requestBody));
        List<ICategory> categories = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            categories.add(new Category(array.getJSONObject(i)));
        }

        return categories;
    }
}
