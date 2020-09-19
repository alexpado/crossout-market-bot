package xo.marketbot.interfaces.common;

/**
 * Interface representing an object holding an ID.
 *
 * @param <ID>
 *         Type of the ID.
 *
 * @author alexpado
 */
public interface Identifiable<ID> {

    /**
     * Retrieve this {@link Identifiable}'s ID.
     *
     * @return An ID.
     */
    ID getId();

    /**
     * Define this {@link Identifiable}'s ID.
     *
     * @param id
     *         An ID
     */
    default void setId(ID id) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
