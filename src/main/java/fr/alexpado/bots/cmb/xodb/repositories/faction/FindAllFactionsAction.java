package fr.alexpado.bots.cmb.xodb.repositories.faction;

import fr.alexpado.bots.cmb.entities.game.Faction;
import fr.alexpado.bots.cmb.interfaces.game.IFaction;
import fr.alexpado.bots.cmb.xodb.XoDB;
import fr.alexpado.lib.rest.RestAction;
import fr.alexpado.lib.rest.enums.RequestMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FindAllFactionsAction extends RestAction<List<IFaction>> {

    private final XoDB xoDB;

    public FindAllFactionsAction(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    @Override
    public @NotNull RequestMethod getRequestMethod() {

        return RequestMethod.GET;
    }

    @Override
    public @NotNull String getRequestURL() {

        return String.format("%s/factions", this.xoDB.getRootUrl());
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
    public List<IFaction> convert(byte[] requestBody) {

        JSONArray      array    = new JSONArray(new String(requestBody));
        List<IFaction> factions = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            factions.add(new Faction(array.getJSONObject(i)));
        }

        return factions;
    }
}
