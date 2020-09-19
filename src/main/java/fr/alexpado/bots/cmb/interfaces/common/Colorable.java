package fr.alexpado.bots.cmb.interfaces.common;

import java.awt.*;

/**
 * Interface representing an object holding a {@link Color} instance.
 *
 * @author alexpado
 */
public interface Colorable {

    /**
     * Retrieve this {@link Colorable}'s {@link Color} instance.
     *
     * @return A {@link Color}.
     */
    Color getColor();

    /**
     * Define this {@link Colorable}'s {@link Color} instance.
     *
     * @param color
     *         A {@link Color}.
     */
    default void setColor(Color color) {

        throw new UnsupportedOperationException("Unable to set value: This value is read-only.");
    }

}
