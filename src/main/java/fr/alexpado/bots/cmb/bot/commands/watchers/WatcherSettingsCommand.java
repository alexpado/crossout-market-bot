package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.game.Item;
import fr.alexpado.bots.cmb.repositories.WatcherRepository;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WatcherSettingsCommand extends AbstractWatcherCommand {

    public WatcherSettingsCommand(JDAModule module) {
        super(module, "watchersettings");
    }

    @Override
    public List<String> getLanguageKeys() {
        return Arrays.asList(
                Translation.WATCHER_WRONG_TYPE,
                Translation.WATCHER_WRONG_VALUE,
                Translation.WATCHER_WRONG_PRICE,
                Translation.WATCHER_UPDATED,
                Translation.WATCHER_NOT_FOUND,
                Translation.WATCHER_FORBIDDEN,
                Translation.WATCHER_ITEM_REMOVED,
                Translation.GENERAL_ERROR
        );
    }

    @Override
    public void execute(CommandEvent event, Message message) {

        WatcherRepository wr = this.getConfig().watcherRepository;
        ItemEndpoint endpoint = new ItemEndpoint(this.getConfig().getApiHost());

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

        Optional<Item> optionalItem = endpoint.getOne(watcher.getItemId());

        if (!optionalItem.isPresent()) {
            this.sendError(message, this.getTranslation(Translation.GENERAL_ERROR));
            return;
        }
        Item item = optionalItem.get();

        if (item.isRemoved()) {
            this.sendWarn(message, this.getTranslation(Translation.WATCHER_ITEM_REMOVED));
            return;
        }


        if (this.updateWatcher(event, watcher, item, message, true)) {
            this.sendInfo(message, this.getTranslation(Translation.WATCHER_UPDATED));
        }
    }
}
