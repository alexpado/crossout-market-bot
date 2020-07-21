package fr.alexpado.bots.cmb.cleaning.entities.game;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Nameable;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.ICategory;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IItem;
import org.json.JSONObject;

/**
 * Class implementing the {@link ICategory} interface representing an {@link IItem} category.
 * <p>
 * All fields are read-only because these data aren't meant to be edited, as they come from the CrossoutDB's API.
 *
 * @author alexpado
 */
public class Category implements ICategory {

    private final Integer id;
    private final String  name;

    /**
     * Create a new {@link Category} instance.
     *
     * @param source
     *         JSON containing all values needed to create this {@link Category}.
     */
    public Category(JSONObject source) {

        this.id   = source.getInt("id");
        this.name = source.getString("name");
    }

    /**
     * Retrieve this {@link Identifiable}'s ID.
     *
     * @return The ID.
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
