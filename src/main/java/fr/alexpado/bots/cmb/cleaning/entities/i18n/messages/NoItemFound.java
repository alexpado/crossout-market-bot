package fr.alexpado.bots.cmb.cleaning.entities.i18n.messages;

import fr.alexpado.bots.cmb.cleaning.i18n.TranslationProvider;
import fr.alexpado.jda.services.translations.annotations.I18N;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.annotation.Nonnull;

public class NoItemFound extends EmbedBuilder {

    private final JDA jda;

    @I18N(TranslationProvider.ITEMS_NOTFOUND)
    private String message;

    public NoItemFound(JDA jda) {

        this.jda = jda;
    }

    @Nonnull
    @Override
    public MessageEmbed build() {

        super.setFooter(jda.getSelfUser()
                           .getName() + " // Offical CrossoutDB Bot", "https://cdn.discordapp.com/icons/467736507336097814/52bc61342744c81a7122e899500d27f6.webp");
        super.setDescription(this.message);
        return super.build();
    }
}
