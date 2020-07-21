package fr.alexpado.bots.cmb.cleaning.xodb.item;

import fr.alexpado.bots.cmb.cleaning.XoDB;
import fr.alexpado.bots.cmb.cleaning.entities.game.Item;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IItem;
import fr.alexpado.bots.cmb.cleaning.rest.method.GetRestRequest;
import org.json.JSONObject;

public class FindItemByIdAction extends GetRestRequest<IItem> {

    private final XoDB    xoDB;
    private final Integer id;

    public FindItemByIdAction(XoDB xoDB, Integer id) {

        this.xoDB = xoDB;
        this.id   = id;
    }


    /**
     * Retrieve the string version that will be used to send the request.
     *
     * @return An URL as String
     */
    @Override
    public String getTargetUrl() {

        return String.format("%s/item/%s", this.xoDB.getRootUrl(), this.id);
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
    public IItem convert(String output) {

        return new Item(this.xoDB, new JSONObject(output));
    }
}
