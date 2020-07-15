package fr.alexpado.bots.cmb.cleaning.interfaces.common;

/**
 * Interface representing an object holding a description.
 *
 * @author alexpado
 */
public interface Describable {

    /**
     * Retrieve this {@link Describable}'s description.
     *
     * @return The description.
     */
    String getDescription();

    /**
     * Define this {@link Describable}'s description.
     *
     * @param description
     *         The description.
     */
    default void setDescription(String description) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
