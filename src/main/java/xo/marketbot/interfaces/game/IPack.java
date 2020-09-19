package xo.marketbot.interfaces.game;

import xo.marketbot.interfaces.common.Identifiable;
import xo.marketbot.interfaces.common.Marchantable;
import xo.marketbot.interfaces.common.Nameable;

/**
 * Interface representing a Crossout's pack.
 *
 * @author alexpado
 */
public interface IPack extends Identifiable<Integer>, Nameable, Marchantable {

    /**
     * Retrieve this {@link IPack}'s key identifier.
     *
     * @return A string
     */
    String getKey();

    /**
     * Define this {@link IPack}'s key identifier.
     *
     * @param key
     *         A string
     */
    default void setKey(String key) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

    /**
     * Retrieve this {@link IPack}'s price in USD.
     *
     * @return The price
     */
    Double getPriceUSD();

    /**
     * Retrieve this {@link IPack}'s price in USD.
     *
     * @param price
     *         The price
     */
    default void setPriceUSD(Double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

    /**
     * Retrieve this {@link IPack}'s price in EUR.
     *
     * @return The price
     */
    Double getPriceEUR();

    /**
     * Retrieve this {@link IPack}'s price in EUR.
     *
     * @param price
     *         The price
     */
    default void setPriceEUR(Double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

    /**
     * Retrieve this {@link IPack}'s price in GBP.
     *
     * @return The price
     */
    Double getPriceGBP();

    /**
     * Retrieve this {@link IPack}'s price in GBP.
     *
     * @param price
     *         The price
     */
    default void setPriceGBP(Double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

    /**
     * Retrieve this {@link IPack}'s price in RUB.
     *
     * @return The price
     */
    Double getPriceRUB();

    /**
     * Retrieve this {@link IPack}'s price in RUB.
     *
     * @param price
     *         The price
     */
    default void setPriceRUB(Double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

    /**
     * Retrieve this {@link IPack}'s value in coins.
     *
     * @return The value
     */
    Double getRawCoins();

    /**
     * Retrieve this {@link IPack}'s value in coins.
     *
     * @param coins
     *         The value
     */
    default void setRawCoins(Double coins) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
