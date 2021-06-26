package xo.marketbot.i18n.messages.categories;

import net.dv8tion.jda.api.entities.MessageEmbed;
import xo.marketbot.entities.interfaces.game.ICategory;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.i18n.messages.BaseEmbedPage;
import xo.marketbot.library.services.translations.annotations.I18N;

import java.util.List;

public class CategoryListEmbed extends BaseEmbedPage<ICategory> {

    @I18N(TranslationProvider.CATEGORIES_INVALID)
    private String message;

    @I18N(TranslationProvider.GENERAL_INVITE)
    private String invite;

    public CategoryListEmbed(List<ICategory> items) {

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
    public MessageEmbed.Field getFieldFor(int index, ICategory item) {

        return new MessageEmbed.Field(item.getName(), "", false);
    }
}
