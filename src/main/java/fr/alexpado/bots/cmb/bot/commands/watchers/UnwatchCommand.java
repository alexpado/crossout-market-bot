package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.bot.BotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.repositories.WatcherRepository;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UnwatchCommand extends BotCommand {

    public UnwatchCommand(JDAModule module) {
        super(module, "unwatch");
    }

    @Override
    public List<String> getLanguageKeys() {
        return Arrays.asList(
                Translation.WATCHER_REMOVED,
                Translation.WATCHER_NOT_FOUND,
                Translation.WATCHER_FORBIDDEN
        );
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        WatcherRepository wr = this.getConfig().watcherRepository;

        int watcherId = Integer.parseInt(event.getArgs().get(0));

        Optional<Watcher> optionalWatcher = wr.findById(watcherId);

        if (!optionalWatcher.isPresent()) {
            this.sendError(message, this.getTranslation(Translation.WATCHER_NOT_FOUND));
            return;
        }

        Watcher watcher = optionalWatcher.get();

        if (watcher.getUser().getId() != this.getDiscordUser(event).getId()) {
            this.sendError(message, this.getTranslation(Translation.WATCHER_FORBIDDEN));
            return;
        }

        wr.delete(watcher);

        this.sendInfo(message, this.getTranslation(Translation.WATCHER_REMOVED));
    }
}
