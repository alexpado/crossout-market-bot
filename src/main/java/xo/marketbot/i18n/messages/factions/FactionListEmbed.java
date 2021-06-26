package xo.marketbot.i18n.messages.factions;

import net.dv8tion.jda.api.entities.MessageEmbed;
import xo.marketbot.entities.interfaces.game.IFaction;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.i18n.messages.BaseEmbedPage;
import xo.marketbot.library.services.translations.annotations.I18N;

import java.util.List;

public class FactionListEmbed extends BaseEmbedPage<IFaction> {

    @I18N(TranslationProvider.FACTIONS_INVALID)
    private String message;

    @I18N(TranslationProvider.GENERAL_INVITE)
    private String invite;

    public FactionListEmbed(List<IFaction> items) {

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
    public MessageEmbed.Field getFieldFor(int index, IFaction item) {

        return new MessageEmbed.Field(item.getName(), "", false);
    }
}
