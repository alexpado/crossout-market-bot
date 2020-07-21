package fr.alexpado.bots.cmb.cleaning.xodb.item;

import fr.alexpado.bots.cmb.cleaning.XoDB;
import fr.alexpado.bots.cmb.cleaning.entities.game.Item;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IItem;
import fr.alexpado.bots.cmb.cleaning.rest.method.GetRestRequest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindAllItemsAction extends GetRestRequest<List<IItem>> {

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

    /**
     * Retrieve the string version that will be used to send the request.
     *
     * @return An URL as String
     */
    @Override
    public String getTargetUrl() {

        return String.format("%s/items", this.xoDB.getRootUrl());
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
    public List<IItem> convert(String output) {

        JSONArray   array = new JSONArray(output);
        List<IItem> items = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            items.add(new Item(this.xoDB, array.getJSONObject(i)));
        }

        return items;
    }

    /**
     * Retrieve a map to use as Query parameters. Can be null or empty;
     *
     * @return A map
     */
    @Override
    public Map<String, String> getQueryParams() {

        return this.searchParams;
    }
}
