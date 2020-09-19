package fr.alexpado.bots.cmb.entities.i18n.messages;

import fr.alexpado.bots.cmb.i18n.TranslationProvider;
import fr.alexpado.bots.cmb.tools.DiscordEmbed;
import fr.alexpado.jda.services.translations.annotations.I18N;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.Nonnull;

public class XoDBUnavailableMessage extends DiscordEmbed {

    @I18N(TranslationProvider.XODB_OFFLINE)
    private String message;

    public XoDBUnavailableMessage(JDA jda) {

        super(jda);
    }

    @Nonnull
    @Override
    public MessageEmbed build() {

        super.setDescription(this.message);
        return super.build();
    }

}
