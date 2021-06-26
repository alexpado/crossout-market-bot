package xo.marketbot.i18n.messages.rartities;

import net.dv8tion.jda.api.entities.MessageEmbed;
import xo.marketbot.entities.interfaces.game.IRarity;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.i18n.messages.BaseEmbedPage;
import xo.marketbot.library.services.translations.annotations.I18N;

import java.util.List;

public class RarityListEmbed extends BaseEmbedPage<IRarity> {

    @I18N(TranslationProvider.RARITIES_INVALID)
    private String message;

    @I18N(TranslationProvider.GENERAL_INVITE)
    private String invite;

    public RarityListEmbed(List<IRarity> items) {

        super(items);
    }

    @Override
    public String getInvite() {

        return this.invite;
    }

    @Override
    public String getTitle() {

        return this.message;
    }

    @Override
    public MessageEmbed.Field getFieldFor(int index, IRarity item) {

        return new MessageEmbed.Field(item.getName(), "", false);
    }
}
