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

import java.util.HashMap;
import java.util.List;

public class WatchCommand extends AbstractWatcherCommand {

    public WatchCommand(JDAModule module) {
        super(module, "watch");
    }

    @Override
    public List<String> getLanguageKeys() {
        return Utilities.mergeList(super.getLanguageKeys(),
                Translation.WATCHERS_WRONG_FOR,
                Translation.ITEMS_NOTFOUND,
                Translation.ITEMS_MULTIPLE,
                Translation.WATCHERS_NEW
        );
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        int whenIndex = event.getArgs().indexOf("when");
        int everyIndex = event.getArgs().indexOf("every");
        int forIndex = event.getArgs().indexOf("for");

        String itemName;

        if (forIndex != -1) {
            itemName = String.join(" ", event.getArgs().subList(forIndex + 1, event.getArgs().size()));
        } else if (whenIndex != -1 || everyIndex != -1) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_WRONG_FOR));
            return;
        } else {
            itemName = String.join(" ", event.getArgs().subList(1, event.getArgs().size()));
        }

        ItemEndpoint itemEndpoint = new ItemEndpoint(this.getConfig().getApiHost());
        HashMap<String, String> map = new HashMap<>();
        map.put("query", itemName);

        List<Item> items = itemEndpoint.search(map);

        if (items.size() == 0) {
            this.sendError(message, this.getTranslation(Translation.ITEMS_NOTFOUND));
            return;
        } else if (items.size() > 1) {
            this.sendError(message, this.getTranslation(Translation.ITEMS_MULTIPLE));
            return;
        }

        Item item = items.get(0);
        Watcher watcher = new Watcher();
        watcher.loadItem(item);

        if (this.updateWatcher(event, watcher, message, false)) {
            this.sendInfo(message, this.getTranslation(Translation.WATCHERS_NEW));
        }
    }
}
