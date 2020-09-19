package xo.marketbot.interfaces.common;

/**
 * Interface representing an object holding information about a repetitive action.
 *
 * @param <UNIT>
 *         The type of the time.
 *
 * @author alexpado
 */
public interface Repeatable<UNIT> {

    /**
     * Retrieve this {@link Repeatable}'s repetition interval.
     *
     * @return The time.
     */
    UNIT getRepeatEvery();

    /**
     * Define this {@link Repeatable}'s repetition interval.
     *
     * @param unit
     *         The time.
     */
    default void setRepeatEvery(UNIT unit) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
