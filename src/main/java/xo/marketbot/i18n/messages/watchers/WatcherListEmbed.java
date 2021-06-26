package xo.marketbot.i18n.messages.watchers;

import net.dv8tion.jda.api.entities.MessageEmbed;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.i18n.messages.BaseEmbedPage;
import xo.marketbot.library.services.translations.annotations.I18N;

import java.util.List;

public class WatcherListEmbed extends BaseEmbedPage<Watcher> {

    @I18N(TranslationProvider.WATCHERS_LIST)
    private String message;

    @I18N(TranslationProvider.GENERAL_INVITE)
    private String invite;

    public WatcherListEmbed(List<Watcher> items) {

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
    public MessageEmbed.Field getFieldFor(int index, Watcher item) {

        return new MessageEmbed.Field(String.format("%s. %s", item.getId(), item.getName()), item.getDescription(), false);
    }
}
