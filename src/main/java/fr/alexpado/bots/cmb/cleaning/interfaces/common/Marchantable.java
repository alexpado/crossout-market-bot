package fr.alexpado.bots.cmb.cleaning.interfaces.common;

/**
 * Interface representing an object holding market data.
 *
 * @author alexpado
 */
public interface Marchantable {

    /**
     * Retrieve the amount of money needed to buy this {@link Marchantable}.
     *
     * @return The buy price
     */
    double getBuyPrice();

    /**
     * Define the amount of money needed to buy this {@link Marchantable}.
     *
     * @param price
     *         The buy price.
     */
    default void setBuyPrice(double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

    /**
     * Retrieve the amount of money obtainable by selling this {@link Marchantable}.
     *
     * @return The sell price
     */
    double getSellPrice();

    /**
     * Define the amount of money obtainable by selling this {@link Marchantable}
     *
     * @param price
     *         The sell price
     */
    default void setSellPrice(double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
