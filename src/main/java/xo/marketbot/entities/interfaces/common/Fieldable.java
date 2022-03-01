package xo.marketbot.entities.interfaces.common;

import net.dv8tion.jda.api.entities.MessageEmbed;

public interface Fieldable {

    /**
     * Convert this {@link Fieldable} to a {@link MessageEmbed.Field}.
     *
     * @return A {@link MessageEmbed.Field}.
     */
    MessageEmbed.Field toField();

}
