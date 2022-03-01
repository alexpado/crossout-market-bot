package xo.marketbot.entities.interfaces.common;

/**
 * Interface representing an object holding information about update time.
 *
 * @param <UNIT>
 *         The type of the time
 *
 * @author alexpado
 */
public interface Updatable<UNIT> {

    /**
     * Retrieve this {@link Updatable}'s last update time.
     *
     * @return The time
     */
    UNIT getLastUpdate();

    /**
     * Define this {@link Updatable}'s last update time.
     *
     * @param time
     *         The time.
     */
    default void setLastUpdate(UNIT time) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
