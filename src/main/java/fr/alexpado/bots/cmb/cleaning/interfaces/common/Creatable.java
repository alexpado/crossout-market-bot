package fr.alexpado.bots.cmb.cleaning.interfaces.common;

/**
 * Interface representing an object holding information about creation time.
 *
 * @param <UNIT>
 *         The type of the time
 *
 * @author alexpado
 */
public interface Creatable<UNIT> {

    /**
     * Retrieve this {@link Creatable}'s creation time.
     *
     * @return The time
     */
    UNIT getCreatedAt();

    /**
     * Define this {@link Creatable}'s creation time.
     *
     * @param time
     *         The time.
     */
    default void setCreatedAt(UNIT time) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
