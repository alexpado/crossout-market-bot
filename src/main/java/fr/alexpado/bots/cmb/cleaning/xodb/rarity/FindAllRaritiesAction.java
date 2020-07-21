package fr.alexpado.bots.cmb.cleaning.xodb.rarity;

import fr.alexpado.bots.cmb.cleaning.XoDB;
import fr.alexpado.bots.cmb.cleaning.entities.game.Rarity;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IRarity;
import fr.alexpado.bots.cmb.cleaning.rest.method.GetRestRequest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FindAllRaritiesAction extends GetRestRequest<List<IRarity>> {

    private final XoDB xoDB;

    public FindAllRaritiesAction(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    /**
     * Retrieve the string version that will be used to send the request.
     *
     * @return An URL as String
     */
    @Override
    public String getTargetUrl() {

        return String.format("%s/rarities", this.xoDB.getRootUrl());
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
    public List<IRarity> convert(String output) {

        JSONArray     array    = new JSONArray(output);
        List<IRarity> rarities = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            rarities.add(new Rarity(array.getJSONObject(i)));
        }

        return rarities;
    }
}
