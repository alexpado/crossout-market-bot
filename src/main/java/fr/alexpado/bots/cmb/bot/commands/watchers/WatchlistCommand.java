package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.interfaces.AbstractWatcherCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.discord.DiscordGuild;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.tools.Utilities;
import fr.alexpado.bots.cmb.tools.embed.TranslatableEmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class WatchlistCommand extends AbstractWatcherCommand {


    public WatchlistCommand(JDAModule module) {
        super(module, "watchlist");
    }

    @Override
    public List<String> getLanguageKeys() {
        return Utilities.mergeList(super.getLanguageKeys(),
                Translation.WATCHERS_NONE,
                Translation.WATCHERS_LIST
        );
    }

    @Override
    public void execute(CommandEvent event, Message message) {


        DiscordUser user = this.getDiscordUser(event);
        DiscordGuild guild = this.getDiscordGuild(event);

        List<Watcher> watchers = this.getRepository().getFromUser(user);

        if (watchers.size() == 0) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_NONE));
        } else {
            new TranslatableEmbedPage<Watcher>(message, watchers, 10, guild.getLanguage()) {
                @Override
                public EmbedBuilder getEmbed() {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(WatchlistCommand.this.getTranslation(Translation.WATCHERS_LIST));
                    return builder;
                }
            };
        }
    }


}
