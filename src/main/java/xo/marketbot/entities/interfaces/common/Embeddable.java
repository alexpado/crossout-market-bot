package xo.marketbot.entities.interfaces.common;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

public interface Embeddable {

    /**
     * Retrieve an {@link EmbedBuilder} representation of the current {@link Embeddable} instance.
     *
     * @param jda
     *         {@link JDA} instance to use to access the current bot state.
     *
     * @return An {@link EmbedBuilder}.
     */
    EmbedBuilder toEmbed(JDA jda);

}
