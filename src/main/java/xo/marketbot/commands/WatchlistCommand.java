package xo.marketbot.commands;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import xo.marketbot.commands.meta.WatchlistCommandMeta;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.i18n.messages.watchers.NoWatcherEmbed;
import xo.marketbot.i18n.messages.watchers.WatcherListEmbed;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.services.commands.DiscordCommand;
import xo.marketbot.library.services.commands.annotations.Command;
import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;
import xo.marketbot.library.services.translations.Translator;
import xo.marketbot.repositories.WatcherRepository;

import java.util.List;

@Component
public class WatchlistCommand extends DiscordCommand {

    private final WatcherRepository   repository;
    private final TranslationProvider translationProvider;

    /**
     * Creates a new {@link DiscordCommand} instance and register it immediately.
     *
     * @param discordBot
     *         The {@link IDiscordBot} associated with this command.
     * @param repository
     *         The {@link JpaRepository} allowing database interaction with {@link Watcher}
     * @param translationProvider
     *         The {@link TranslationProvider} to use to translate all {@link Watcher} entities.
     */
    protected WatchlistCommand(IDiscordBot discordBot, WatcherRepository repository, TranslationProvider translationProvider) {

        super(discordBot);
        this.repository          = repository;
        this.translationProvider = translationProvider;
    }

    /**
     * Retrieve the command meta for this {@link ICommand}.
     *
     * @return An {@link ICommandMeta} instance.
     */
    @Override
    public ICommandMeta getMeta() {

        return new WatchlistCommandMeta();
    }

    @Command("match...")
    public Object showWatchlist(ICommandContext context) throws Exception {

        UserEntity    user     = context.getUserEntity();
        List<Watcher> watchers = this.repository.findAllByOwnerId(user.getId());

        for (Watcher watcher : watchers) {
            Translator.translate(this.translationProvider, context.getChannelEntity().getEffectiveLanguage(), watcher);
        }

        if (!watchers.isEmpty()) {
            return new WatcherListEmbed(watchers);
        }

        return new NoWatcherEmbed();
    }
}
