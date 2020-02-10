package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.interfaces.command.WatcherCommandGroup;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UnwatchCommand extends WatcherCommandGroup {

    public UnwatchCommand(JDAModule module) {
        super(module, "unwatch");
    }

    @Override
    public List<String> getRequiredTranslation() {
        List<String> requiredTranslations = new ArrayList<>(super.getRequiredTranslation());
        requiredTranslations.addAll(Arrays.asList(
                Translation.WATCHERS_WRONG_ID,
                Translation.WATCHERS_UNWATCH
        ));
        return requiredTranslations;
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        int watcherId;
        try {
            watcherId = Integer.parseInt(event.getArgs().get(0));
        } catch (NumberFormatException e) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_WRONG_ID));
            return;
        }
        Optional<Watcher> optionalWatcher = this.getWatcher(message, this.getDiscordUser(), watcherId);

        if (optionalWatcher.isPresent()) {
            this.getRepository().delete(optionalWatcher.get());
            this.sendInfo(message, this.getTranslation(Translation.WATCHERS_UNWATCH));
        }
    }
}
