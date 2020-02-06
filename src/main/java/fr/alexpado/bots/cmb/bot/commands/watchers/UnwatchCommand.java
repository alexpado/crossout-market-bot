package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.interfaces.AbstractWatcherCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.tools.Utilities;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Optional;

public class UnwatchCommand extends AbstractWatcherCommand {

    public UnwatchCommand(JDAModule module) {
        super(module, "unwatch");
    }

    @Override
    public List<String> getLanguageKeys() {
        return Utilities.mergeList(super.getLanguageKeys(),
                Translation.WATCHERS_NOTFOUND,
                Translation.WATCHERS_FORBIDDEN,
                Translation.WATCHERS_UNWATCH
        );
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        int watcherId = Integer.parseInt(event.getArgs().get(0));
        Optional<Watcher> optionalWatcher = this.getWatcher(message, this.getDiscordUser(event), watcherId);

        if (optionalWatcher.isPresent()) {
            this.getRepository().delete(optionalWatcher.get());
            this.sendInfo(message, this.getTranslation(Translation.WATCHERS_UNWATCH));
        }
    }
}
