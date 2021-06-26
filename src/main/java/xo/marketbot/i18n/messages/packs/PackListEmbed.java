package xo.marketbot.i18n.messages.packs;

import net.dv8tion.jda.api.entities.MessageEmbed;
import xo.marketbot.entities.interfaces.game.IPack;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.i18n.messages.BaseEmbedPage;
import xo.marketbot.library.services.translations.annotations.I18N;

import java.util.List;

public class PackListEmbed extends BaseEmbedPage<IPack> {

    @I18N(TranslationProvider.PACKS_LIST)
    private String message;

    @I18N(TranslationProvider.GENERAL_INVITE)
    private String invite;

    public PackListEmbed(List<IPack> items) {

        super(items);
    }

    @Override
    public String getInvite() {

        return this.invite;
    }

    @Override
    public String getTitle() {

        return "\uD83D\uDD0D " + this.message;
    }

    @Override
    public MessageEmbed.Field getFieldFor(int index, IPack item) {


        return new MessageEmbed.Field(item.getName(), String.format("%.2f USD • %.2f EUR • %.2f GBP • %.2f RUB", item.getPriceUSD() / 100, item.getPriceEUR() / 100, item.getPriceGBP() / 100, item.getPriceRUB() / 100), false);
    }
}
