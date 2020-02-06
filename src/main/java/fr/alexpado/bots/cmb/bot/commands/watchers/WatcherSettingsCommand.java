package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.interfaces.AbstractWatcherCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.game.Item;
import fr.alexpado.bots.cmb.tools.Utilities;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Optional;

public class WatcherSettingsCommand extends AbstractWatcherCommand {

    public WatcherSettingsCommand(JDAModule module) {
        super(module, "watchersettings");
    }

    @Override
    public List<String> getLanguageKeys() {
        return Utilities.mergeList(super.getLanguageKeys(),
                Translation.WATCHERS_NOTFOUND,
                Translation.WATCHERS_FORBIDDEN,
                Translation.GENERAL_ERROR,
                Translation.WATCHERS_REMOVED,
                Translation.WATCHERS_UPDATED
        );
    }

    @Override
    public void execute(CommandEvent event, Message message) {

        ItemEndpoint endpoint = new ItemEndpoint(this.getConfig().getApiHost());

        int watcherId = Integer.parseInt(event.getArgs().get(0));
        Optional<Watcher> optionalWatcher = this.getWatcher(message, this.getDiscordUser(event), watcherId);

        if (!optionalWatcher.isPresent()) {
            return;
        }

        Watcher watcher = optionalWatcher.get();

        Optional<Item> optionalItem = endpoint.getOne(watcher.getItemId());

        if (!optionalItem.isPresent()) {
            this.sendError(message, this.getTranslation(Translation.GENERAL_ERROR));
            return;
        }
        Item item = optionalItem.get();

        if (item.isRemoved()) {
            this.sendWarn(message, this.getTranslation(Translation.WATCHERS_REMOVED));
            return;
        }


        if (this.updateWatcher(event, watcher, message, true)) {
            this.sendInfo(message, this.getTranslation(Translation.WATCHERS_UPDATED));
        }
    }
}
