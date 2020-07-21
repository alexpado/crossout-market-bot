package fr.alexpado.bots.cmb.cleaning.entities.game;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Nameable;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IItem;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IType;
import org.json.JSONObject;

/**
 * Class implementing the {@link IType} interface representing an {@link IItem} faction.
 * <p>
 * All fields are read-only because these data aren't meant to be edited, as they come from the CrossoutDB's API.
 *
 * @author alexpado
 */
public class Type implements IType {

    private final Integer id;
    private final String  name;

    /**
     * Create a new {@link Type} instance.
     *
     * @param source
     *         JSON containing all values needed to create this {@link Type}
     */
    public Type(JSONObject source) {

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
