package xo.marketbot.xodb.repositories.type;

import fr.alexpado.lib.rest.RestAction;
import fr.alexpado.lib.rest.enums.RequestMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import xo.marketbot.entities.game.Type;
import xo.marketbot.interfaces.game.IType;
import xo.marketbot.xodb.XoDB;

import java.util.ArrayList;
import java.util.List;

public class FindAllTypesAction extends RestAction<List<IType>> {

    private final XoDB xoDB;

    public FindAllTypesAction(XoDB xoDB) {

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
     * Retrieve the request URL to which the request should be sent.
     *
     * @return The request URL.
     */
    @Override
    public @NotNull String getRequestURL() {

        return String.format("%s/types", this.xoDB.getRootUrl());
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
    public List<IType> convert(byte[] requestBody) {

        JSONArray   array = new JSONArray(new String(requestBody));
        List<IType> types = new ArrayList<>();

        for (int i = 0 ; i < array.length() ; i++) {
            types.add(new Type(array.getJSONObject(i)));
        }

        return types;
    }

}
