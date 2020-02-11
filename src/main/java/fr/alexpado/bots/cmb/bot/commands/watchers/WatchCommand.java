package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.enums.WatcherType;
import fr.alexpado.bots.cmb.interfaces.command.WatcherCommandGroup;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.game.Item;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WatchCommand extends WatcherCommandGroup {

    public WatchCommand(JDAModule module) {
        super(module, "watch");
    }

    @Override
    public List<String> getRequiredTranslation() {
        List<String> requiredTranslations = new ArrayList<>(super.getRequiredTranslation());
        requiredTranslations.addAll(Arrays.asList(
                Translation.WATCHERS_WRONG_FOR,
                Translation.ITEMS_NOTFOUND,
                Translation.ITEMS_MULTIPLE,
                Translation.WATCHERS_NEW
        ));
        return requiredTranslations;
    }


    @Override
    public void execute(CommandEvent event, Message message) {
        Optional<String> optionalItemName = this.getItemName(event);

        if (!optionalItemName.isPresent()) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_WRONG_FOR));
            return;
        }

        ItemEndpoint itemEndpoint = new ItemEndpoint(this.getConfig());
        List<Item> items = itemEndpoint.searchByName(optionalItemName.get(), this.getDiscordUser().getLanguage());

        Item item = null;

        if (items.size() == 0) {
            this.sendError(message, this.getTranslation(Translation.ITEMS_NOTFOUND));
            return;
        } else if (items.size() > 1) {
            // Try to find a perfect match.
            for (Item el : items) {
                if (el.getName().equalsIgnoreCase(optionalItemName.get())) {
                    // Perfect match !
                    item = el;
                    break;
                }
            }

            if (item == null) {
                this.sendError(message, this.getTranslation(Translation.ITEMS_MULTIPLE));
                return;
            }
        } else {
            item = items.get(0);
        }

        Watcher watcher = Watcher.createFor(this.getDiscordUser(), item);

        Optional<WatcherType> optionalWatcherType = this.getType(event);
        Optional<Float> optionalPrice = this.getPrice(event);
        Optional<Long> optionalInterval = this.getTime(event);

        if (optionalWatcherType.isPresent()) {
            WatcherType watcherType = optionalWatcherType.get();

            if (watcherType == WatcherType.UNKNOWN) {
                this.sendError(message, Translation.WATCHERS_WRONG_TYPE);
                return;
            }
            watcher.setWatcherType(watcherType.getId());

            if (watcherType != WatcherType.NORMAL) {
                if (!optionalPrice.isPresent()) {
                    this.sendError(message, Translation.WATCHERS_WRONG_PRICE);
                    return;
                }
                Float price = optionalPrice.get();
                watcher.setPrice(price);
            }
        }

        optionalInterval.ifPresent(watcher::setRepeatEvery);

        this.getRepository().save(watcher);
        this.sendInfo(message, this.getTranslation(Translation.WATCHERS_NEW));
    }
}
