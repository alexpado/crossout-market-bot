package xo.marketbot.interfaces.common;

/**
 * Interface representing an object being owned by another object.
 *
 * @param <T>
 *         Type of the owner object.
 *
 * @author alexpado
 */
public interface Ownable<T> {

    /**
     * Retrieve this {@link Ownable}'s owner.
     *
     * @return The owner.
     */
    T getOwner();

    /**
     * Define this {@link Ownable}'s owner.
     *
     * @param owner
     *         The owner.
     */
    default void setOwner(T owner) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
