package fr.alexpado.bots.cmb.cleaning.interfaces.common;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

public interface Embeddable {

    /**
     * Retrieve an {@link EmbedBuilder} representation of the current {@link Embeddable} instance.
     *
     * @param jda
     *         {@link JDA} instance to use to access the current bot state.
     * @param language
     *         The language to use when translating the embed.
     *
     * @return An {@link EmbedBuilder}.
     */
    EmbedBuilder toEmbed(JDA jda, String language);

}
