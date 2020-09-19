package xo.marketbot.entities.game;

import org.json.JSONObject;
import xo.marketbot.entities.interfaces.common.Identifiable;
import xo.marketbot.entities.interfaces.common.Nameable;
import xo.marketbot.entities.interfaces.game.IFaction;
import xo.marketbot.entities.interfaces.game.IItem;

/**
 * Class implementing the {@link IFaction} interface representing an {@link IItem} faction.
 * <p>
 * All fields are read-only because these data aren't meant to be edited, as they come from the CrossoutDB's API.
 *
 * @author alexpado
 */
public class Faction implements IFaction {

    private final Integer id;
    private final String  name;

    /**
     * Create a new {@link Faction} instance.
     *
     * @param source
     *         JSON containing all values needed to create this {@link Faction}.
     */
    public Faction(JSONObject source) {

        this.id   = source.getInt("id");
        this.name = source.getString("name");
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
