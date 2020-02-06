package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.interfaces.BotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.discord.DiscordGuild;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.repositories.WatcherRepository;
import fr.alexpado.bots.cmb.tools.embed.TranslatableEmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;

public class WatchlistCommand extends BotCommand {

    private WatcherRepository repository;

    public WatchlistCommand(JDAModule module) {
        super(module, "watchlist");
        this.repository = this.getConfig().watcherRepository;
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        DiscordUser user = this.getDiscordUser(event);
        DiscordGuild guild = this.getDiscordGuild(event);

        List<Watcher> watchers = repository.getFromUser(user);

        if (watchers.size() == 0) {
            this.sendError(message, this.getTranslation(Translation.WATCHER_EMPTY));
        } else {
            new TranslatableEmbedPage<Watcher>(message, watchers, 10, guild.getLanguage()) {
                @Override
                public EmbedBuilder getEmbed() {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(WatchlistCommand.this.getTranslation(Translation.WATCHER_LIST));
                    return builder;
                }
            };
        }
    }

    @Override
    public List<String> getLanguageKeys() {
        return Arrays.asList(Translation.WATCHER_EMPTY, Translation.WATCHER_LIST);
    }
}
