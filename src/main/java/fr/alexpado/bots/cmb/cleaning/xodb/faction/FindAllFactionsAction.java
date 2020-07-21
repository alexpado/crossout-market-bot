package fr.alexpado.bots.cmb.cleaning.xodb.faction;

import fr.alexpado.bots.cmb.cleaning.XoDB;
import fr.alexpado.bots.cmb.cleaning.entities.game.Faction;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IFaction;
import fr.alexpado.bots.cmb.cleaning.rest.method.GetRestRequest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FindAllFactionsAction extends GetRestRequest<List<IFaction>> {

    private final XoDB xoDB;

    public FindAllFactionsAction(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    /**
     * Retrieve the string version that will be used to send the request.
     *
     * @return An URL as String
     */
    @Override
    public String getTargetUrl() {

        return String.format("%s/factions", this.xoDB.getRootUrl());
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
    public List<IFaction> convert(String output) {

        JSONArray      array    = new JSONArray(output);
        List<IFaction> factions = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            factions.add(new Faction(array.getJSONObject(i)));
        }

        return factions;
    }
}
