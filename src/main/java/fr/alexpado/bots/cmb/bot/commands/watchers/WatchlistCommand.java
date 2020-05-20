package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.interfaces.command.WatcherCommandGroup;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.modules.crossout.models.Watcher;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import fr.alexpado.bots.cmb.tools.TranslatableWatcher;
import fr.alexpado.bots.cmb.tools.embed.TranslatableEmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WatchlistCommand extends WatcherCommandGroup {


    public WatchlistCommand(JDAModule module) {
        super(module, "watchlist");
    }

    @Override
    public List<String> getRequiredTranslation() {
        List<String> requiredTranslations = new ArrayList<>(super.getRequiredTranslation());
        requiredTranslations.addAll(Arrays.asList(
                Translation.WATCHERS_NONE,
                Translation.WATCHERS_LIST
        ));
        return requiredTranslations;
    }


    @Override
    public void execute(CommandEvent event, Message message) throws MissingTranslationException {
        List<Watcher> watchers = this.getRepository().findAllByUser(this.getDiscordUser());

        if (watchers.size() == 0) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_NONE));
        } else {
            List<TranslatableWatcher> translatableWatchers = new ArrayList<>();
            for (Watcher watcher : watchers) {
                translatableWatchers.add(watcher.getTranslatableWatcher(this.getConfig(), this.getEffectiveLanguage()));
            }
            new TranslatableEmbedPage<TranslatableWatcher>(message, translatableWatchers, 10, this.getGuildSettings().getLanguage()) {
                @Override
                public EmbedBuilder getEmbed() {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(WatchlistCommand.this.getTranslation(Translation.WATCHERS_LIST));
                    return builder;
                }
            };
        }
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {
        return null;
    }

}
