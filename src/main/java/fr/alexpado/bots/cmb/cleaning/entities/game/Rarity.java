package fr.alexpado.bots.cmb.cleaning.entities.game;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Colorable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Nameable;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IItem;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IRarity;
import org.json.JSONObject;

import java.awt.*;

/**
 * Class implementing the {@link IRarity} interface representing an {@link IItem} rarity.
 * <p>
 * All fields are read-only because these data aren't meant to be edited, as they come from the CrossoutDB's API.
 *
 * @author alexpado
 */
public class Rarity implements IRarity {

    private final Integer id;
    private final String  name;
    private final Color   color;

    /**
     * Create a new {@link Rarity} instance.
     *
     * @param source
     *         JSON containing all values needed to create this {@link Rarity}.
     */
    public Rarity(JSONObject source) {

        this.id   = source.getInt("id");
        this.name = source.getString("name");

        String colorHex = source.getString("primarycolor");
        int    r        = Integer.parseInt(colorHex.substring(0, 2), 16);
        int    g        = Integer.parseInt(colorHex.substring(2, 4), 16);
        int    b        = Integer.parseInt(colorHex.substring(4, 6), 16);
        this.color = new Color(r, g, b);
    }


    /**
     * Retrieve this {@link Colorable}'s {@link Color} instance.
     *
     * @return A {@link Color}.
     */
    @Override
    public Color getColor() {

        return this.color;
    }

    /**
     * Retrieve this {@link Identifiable}'s ID.
     *
     * @return An ID.
     */
    @Override
    public Integer getId() {

        return this.id;
    }

    /**
     * Retrieve this {@link Nameable}'s name.
     *
     * @return The name.
     */
    @Override
    public String getName() {

        return this.name;
    }
}
