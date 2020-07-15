package fr.alexpado.bots.cmb.cleaning.interfaces.common;

/**
 * Interface representing an object holding a price information.
 *
 * @author alexpado
 */
public interface Priceable {

    /**
     * Retrieve this {@link Priceable}'s price.
     *
     * @return The price.
     */
    double getPrice();

    /**
     * Define this {@link Priceable}'s price.
     *
     * @param price
     *         The price.
     */
    default void setPrice(double price) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
