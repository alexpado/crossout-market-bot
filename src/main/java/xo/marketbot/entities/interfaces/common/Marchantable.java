package xo.marketbot.entities.interfaces.common;

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
    double getMarketSell();

    /**
     * Define the amount of money needed to buy this {@link Marchantable}.
     *
     * @param price
     *         The buy price.
     */
    default void setMarketSell(double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

    /**
     * Retrieve the amount of money obtainable by selling this {@link Marchantable}.
     *
     * @return The sell price
     */
    double getMarketBuy();

    /**
     * Define the amount of money obtainable by selling this {@link Marchantable}
     *
     * @param price
     *         The sell price
     */
    default void setMarketBuy(double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
