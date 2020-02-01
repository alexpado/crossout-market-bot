package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.repositories.WatcherRepository;
import fr.alexpado.bots.cmb.bot.BotCommand;
import fr.alexpado.bots.cmb.libs.TKey;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;

public class WatcherCommand extends BotCommand {

    private WatcherRepository repository;

    public WatcherCommand(JDAModule module) {
        super(module, "watchlist");
        this.repository = this.getConfig().watcherRepository;
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        DiscordUser user = this.getDiscordUser(event);
        List<Watcher> watchers = repository.getFromUser(user);

        if (watchers.size() == 0) {
            this.sendError(message, this.getTranslation(TKey.WATCHER_EMPTY));
        } else {
            new EmbedPage<Watcher>(message, watchers, 10) {
                @Override
                public EmbedBuilder getEmbed() {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(WatcherCommand.this.getTranslation(TKey.WATCHER_LIST));
                    return builder;
                }
            };
        }
    }

    @Override
    public List<String> getLanguageKeys() {
        return Arrays.asList(TKey.WATCHER_EMPTY, TKey.WATCHER_LIST);
    }
}
