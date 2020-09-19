package xo.marketbot.entities.i18n.messages;

import fr.alexpado.jda.services.translations.annotations.I18N;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.tools.DiscordEmbed;

import javax.annotation.Nonnull;

public class EmptyItemListMessage extends DiscordEmbed {

    @I18N(TranslationProvider.ITEMS_NOTFOUND)
    private String message;

    public EmptyItemListMessage(JDA jda) {

        super(jda);
    }

    @Nonnull
    @Override
    public MessageEmbed build() {

        super.setDescription(this.message);
        return super.build();
    }
}
