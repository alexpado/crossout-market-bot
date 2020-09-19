package xo.marketbot.xodb.repositories.item;

import fr.alexpado.lib.rest.RestAction;
import fr.alexpado.lib.rest.enums.RequestMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import xo.marketbot.entities.game.Item;
import xo.marketbot.interfaces.game.IItem;
import xo.marketbot.xodb.XoDB;

public class FindItemByIdAction extends RestAction<IItem> {

    private final XoDB    xoDB;
    private final Integer id;

    public FindItemByIdAction(XoDB xoDB, Integer id) {

        this.xoDB = xoDB;
        this.id   = id;
    }

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

        return String.format("%s/item/%s", this.xoDB.getRootUrl(), this.id);
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
    public IItem convert(byte[] requestBody) {

        return new Item(this.xoDB, new JSONObject(new String(requestBody)));
    }

}
