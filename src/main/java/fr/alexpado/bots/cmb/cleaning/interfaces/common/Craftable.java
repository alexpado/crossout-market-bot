package fr.alexpado.bots.cmb.cleaning.interfaces.common;

import fr.alexpado.bots.cmb.cleaning.interfaces.game.IItem;

/**
 * Interface representing an object holding crafting information.
 *
 * @author alexpado
 */
public interface Craftable {

    /**
     * Checks this {@link Craftable}'s craftable state.
     *
     * @return True if craftable, false instead.
     */
    boolean isCraftable();

    /**
     * Retrieve the amount of money needed to buy every {@link IItem} of this {@link Craftable}'s crafting recipe. The
     * value returned by this method should be ignored if {@link #isCraftable()} returns false.
     *
     * @return Amount of money needed to craft.
     */
    double getBuyCraftPrice();

    /**
     * Define the amount of money needed to buy every {@link IItem} of this {@link Craftable}'s crafting recipe.
     *
     * @param price
     *         Amount of money needed to craft.
     */
    default void setBuyCraftPrice(double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

    /**
     * Retrieve the amount of money obtainable by selling every {@link IItem} of this {@link Craftable}'s crafting
     * recipe. The value returned by this method should be ignored if {@link #isCraftable()} returns false.
     *
     * @return Amount of money obtainable.
     */
    double getSellCraftPrice();

    /**
     * Define the amount of money obtainable by selling every {@link IItem} of this {@link Craftable}'s crafting
     * recipe.
     *
     * @param price
     *         Amount of money obtainable.
     */
    default void setSellCraftPrice(double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
