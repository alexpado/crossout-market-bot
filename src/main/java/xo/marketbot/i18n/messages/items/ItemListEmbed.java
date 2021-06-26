package xo.marketbot.i18n.messages.items;

import net.dv8tion.jda.api.entities.MessageEmbed;
import xo.marketbot.entities.interfaces.game.IItem;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.i18n.messages.BaseEmbedPage;
import xo.marketbot.library.services.translations.annotations.I18N;

import java.util.List;

public class ItemListEmbed extends BaseEmbedPage<IItem> {


    @I18N(TranslationProvider.ITEMS_LIST)
    private String message;

    @I18N(TranslationProvider.GENERAL_INVITE)
    private String invite;

    public ItemListEmbed(List<IItem> items) {

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
    public MessageEmbed.Field getFieldFor(int index, IItem item) {

        return new MessageEmbed.Field(item.getName(), String.format("%s - %s - %s", item.getRarity().getName(), item.getFaction()
                                                                                                                    .getName(), item.getCategory()
                                                                                                                                    .getName()), false);
    }
}
