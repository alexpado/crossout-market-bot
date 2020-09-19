package fr.alexpado.bots.cmb.interfaces.common;

/**
 * Interface representing an object holding a name.
 *
 * @author alexpado
 */
public interface Nameable {

    /**
     * Retrieve this {@link Nameable}'s name.
     *
     * @return The name.
     */
    String getName();

    /**
     * Define this {@link Nameable}'s name.
     *
     * @param name
     *         The name.
     */
    default void setName(String name) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
