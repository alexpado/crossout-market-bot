package fr.alexpado.bots.cmb.cleaning.xodb.pack;

import fr.alexpado.bots.cmb.cleaning.XoDB;
import fr.alexpado.bots.cmb.cleaning.entities.game.Pack;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IPack;
import fr.alexpado.bots.cmb.cleaning.rest.method.GetRestRequest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FindAllPacksAction extends GetRestRequest<List<IPack>> {

    private final XoDB xoDB;

    public FindAllPacksAction(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    /**
     * Retrieve the string version that will be used to send the request.
     *
     * @return An URL as String
     */
    @Override
    public String getTargetUrl() {

        return String.format("%s/packs", this.xoDB.getRootUrl());
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
    public List<IPack> convert(String output) {

        JSONArray   array = new JSONArray(output);
        List<IPack> packs = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            packs.add(new Pack(array.getJSONObject(i)));
        }

        return packs;
    }
}
