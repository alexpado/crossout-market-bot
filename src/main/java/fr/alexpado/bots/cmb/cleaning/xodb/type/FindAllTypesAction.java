package fr.alexpado.bots.cmb.cleaning.xodb.type;

import fr.alexpado.bots.cmb.cleaning.XoDB;
import fr.alexpado.bots.cmb.cleaning.entities.game.Type;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IType;
import fr.alexpado.bots.cmb.cleaning.rest.method.GetRestRequest;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class FindAllTypesAction extends GetRestRequest<List<IType>> {

    private final XoDB xoDB;

    public FindAllTypesAction(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    /**
     * Retrieve the string version that will be used to send the request.
     *
     * @return An URL as String
     */
    @Override
    public String getTargetUrl() {

        return String.format("%s/types", this.xoDB.getRootUrl());
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
    public List<IType> convert(String output) {

        JSONArray   array = new JSONArray(output);
        List<IType> types = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            types.add(new Type(array.getJSONObject(i)));
        }

        return types;
    }
}
