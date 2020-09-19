package xo.marketbot.xodb.repositories.item;


import fr.alexpado.lib.rest.RestAction;
import fr.alexpado.lib.rest.enums.RequestMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import xo.marketbot.entities.game.Item;
import xo.marketbot.interfaces.game.IItem;
import xo.marketbot.xodb.XoDB;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindAllItemsAction extends RestAction<List<IItem>> {

    private final XoDB                xoDB;
    private final Map<String, String> searchParams;

    public FindAllItemsAction(XoDB xoDB) {

        this.xoDB         = xoDB;
        this.searchParams = null;
    }

    public FindAllItemsAction(XoDB xoDB, Map<String, Object> searchParams) {

        this.xoDB         = xoDB;
        this.searchParams = new HashMap<>();

        searchParams.forEach((k, v) -> this.searchParams.put(k, v.toString()));
    }

    @Override
    public @NotNull RequestMethod getRequestMethod() {

        return RequestMethod.GET;
    }

    @Override
    public @NotNull String getRequestURL() {

        return String.format("%s/items", this.xoDB.getRootUrl());
    }

    /**
     * Convert the response's body to the desired type.
     *
     * @param requestBody
     *         The response's body.
     *
     * @return An object of desired type.
     */
    @Override
    public List<IItem> convert(byte[] requestBody) {

        JSONArray   array = new JSONArray(new String(requestBody));
        List<IItem> items = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            items.add(new Item(this.xoDB, array.getJSONObject(i)));
        }

        return items;
    }

    @Override
    public @NotNull Map<String, String> getRequestParameters() {

        return this.searchParams;
    }
}
