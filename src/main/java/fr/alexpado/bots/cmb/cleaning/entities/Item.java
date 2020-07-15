package fr.alexpado.bots.cmb.cleaning.entities;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.*;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IItem;

import java.time.LocalDateTime;

public class Item implements IItem {

    private int     id;
    private String  availableName;
    private boolean removed;
    private boolean craftable;
    private int     sellPrice;
    private int     buyPrice;
    private int     craftingSellSum;
    private int     craftingBuySum;
    private long    lastUpdate;

    /**
     * Checks this {@link Craftable}'s craftable state.
     *
     * @return True if craftable, false instead.
     */
    @Override
    public boolean isCraftable() {

        return this.craftable;
    }

    /**
     * Retrieve the amount of money needed to buy every {@link IItem} of this {@link Craftable}'s crafting recipe. The
     * value returned by this method should be ignored if {@link #isCraftable()} returns false.
     *
     * @return Amount of money needed to craft.
     */
    @Override
    public double getBuyCraftPrice() {

        return this.sellPrice;
    }

    /**
     * Retrieve the amount of money obtainable by selling every {@link IItem} of this {@link Craftable}'s crafting
     * recipe. The value returned by this method should be ignored if {@link #isCraftable()} returns false.
     *
     * @return Amount of money obtainable.
     */
    @Override
    public double getSellCraftPrice() {

        return this.buyPrice;
    }

    /**
     * Retrieve this {@link Describable}'s description.
     *
     * @return The description.
     */
    @Override
    public String getDescription() {

        return null;
    }

    /**
     * Retrieve this {@link Identifiable}'s ID.
     *
     * @return An ID.
     */
    @Override
    public Integer getId() {

        return null;
    }

    /**
     * Retrieve the amount of money needed to buy this {@link Marchantable}.
     *
     * @return The buy price
     */
    @Override
    public double getBuyPrice() {

        return 0;
    }

    /**
     * Retrieve the amount of money obtainable by selling this {@link Marchantable}.
     *
     * @return The sell price
     */
    @Override
    public double getSellPrice() {

        return 0;
    }

    /**
     * Retrieve this {@link Nameable}'s name.
     *
     * @return The name.
     */
    @Override
    public String getName() {

        return null;
    }

    /**
     * Retrieve this {@link Updatable}'s last update time.
     *
     * @return The time
     */
    @Override
    public LocalDateTime getLastUpdate() {

        return null;
    }
}
