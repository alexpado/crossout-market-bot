package fr.alexpado.bots.cmb.xodb.repositories.pack;

import fr.alexpado.bots.cmb.entities.game.Pack;
import fr.alexpado.bots.cmb.interfaces.game.IPack;
import fr.alexpado.bots.cmb.xodb.XoDB;
import fr.alexpado.lib.rest.RestAction;
import fr.alexpado.lib.rest.enums.RequestMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FindAllPacksAction extends RestAction<List<IPack>> {

    private final XoDB xoDB;

    public FindAllPacksAction(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    /**
     * Retrieve the {@link RequestMethod} to use when sending the request.
     *
     * @return The {@link RequestMethod} to use.
     */
    @Override
    public @NotNull RequestMethod getRequestMethod() {

        return RequestMethod.GET;
    }

    /**
     * Retrieve the string version that will be used to send the request.
     *
     * @return An URL as String
     */
    @Override
    public @NotNull String getRequestURL() {

        return String.format("%s/packs", this.xoDB.getRootUrl());
    }

    /**
     * Convert the response body to the desired type for this request.
     *
     * @param requestBody
     *         The response body received.
     *
     * @return The response body as byte array.
     */
    @Override
    public List<IPack> convert(byte[] requestBody) {

        JSONArray   array = new JSONArray(new String(requestBody));
        List<IPack> packs = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            packs.add(new Pack(array.getJSONObject(i)));
        }

        return packs;
    }

}
