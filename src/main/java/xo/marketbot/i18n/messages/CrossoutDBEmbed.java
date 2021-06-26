package xo.marketbot.i18n.messages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import xo.marketbot.library.services.translations.annotations.I18N;

import java.awt.*;

public class CrossoutDBEmbed extends EmbedBuilder {

    @I18N("xodb.offline")
    private String message;

    /**
     * Returns a {@link MessageEmbed MessageEmbed} that has been checked as being valid for sending.
     *
     * @return the built, sendable {@link MessageEmbed}
     *
     * @throws IllegalStateException
     *         If the embed is empty. Can be checked with {@link #isEmpty()}.
     */
    @NotNull
    @Override
    public MessageEmbed build() {

        super.setDescription(this.message);
        super.setColor(Color.ORANGE);

        return super.build();
    }
}
