package xo.marketbot.entities.interfaces.game;

import xo.marketbot.entities.interfaces.common.*;

import java.time.LocalDateTime;

public interface IItem extends Identifiable<Integer>, Nameable, Describable, Updatable<LocalDateTime>, Marchantable, Craftable, Embeddable {

    /**
     * Check if this {@link IItem} has been removed from the game.
     *
     * @return True if removed, false instead.
     */
    boolean isRemoved();

    /**
     * Check if this {@link IItem} is a meta item.
     *
     * @return True if it's a meta item.
     */
    boolean isMeta();

    /**
     * Get the {@link IType} of this {@link IItem}.
     *
     * @return An {@link IType} instance.
     */
    IType getType();

    /**
     * Get the {@link ICategory} of this {@link IItem}.
     *
     * @return An {@link ICategory} instance.
     */
    ICategory getCategory();

    /**
     * Get the {@link IRarity} of this {@link IItem}.
     *
     * @return An {@link IRarity} instance.
     */
    IRarity getRarity();

    /**
     * Get the {@link IFaction} of this {@link IItem}.
     *
     * @return An {@link IFaction} instance.
     */
    IFaction getFaction();

}
