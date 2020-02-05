package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.bot.BotCommand;
import fr.alexpado.bots.cmb.enums.WatcherTypes;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.game.Item;
import fr.alexpado.bots.cmb.repositories.WatcherRepository;
import fr.alexpado.bots.cmb.tools.TimeConverter;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class WatchCommand extends BotCommand {

    public WatchCommand(JDAModule module) {
        super(module, "watch");
    }

    @Override
    public List<String> getLanguageKeys() {
        return Arrays.asList(
                Translation.WATCHER_WRONG_TYPE,
                Translation.WATCHER_WRONG_VALUE,
                Translation.WATCHER_WRONG_PRICE,
                Translation.WATCHER_ADDED,
                Translation.ITEM_NOT_FOUND,
                Translation.WATCHER_WRONG_FOR,
                Translation.WATCHER_MULTIPLE_ITEM
        );
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        WatcherRepository wr = this.getConfig().watcherRepository;

        Integer whenIndex = event.getArgs().indexOf("when");
        Integer priceIndex = event.getArgs().indexOf("price");
        Integer everyIndex = event.getArgs().indexOf("every");
        Integer forIndex = event.getArgs().indexOf("for");

        String itemName;

        if (forIndex != -1) {
            itemName = String.join(" ", event.getArgs().subList(forIndex + 1, event.getArgs().size()));
        } else if (whenIndex != -1 || everyIndex != -1) {
            this.sendError(message, this.getTranslation(Translation.WATCHER_WRONG_FOR));
            return;
        } else {
            itemName = String.join(" ", event.getArgs().subList(1, event.getArgs().size()));
        }

        ItemEndpoint itemEndpoint = new ItemEndpoint(this.getConfig().getApiHost());
        HashMap<String, String> map = new HashMap<>();
        map.put("query", itemName);

        List<Item> items = itemEndpoint.search(map);

        if (items.size() == 0) {
            this.sendError(message, this.getTranslation(Translation.ITEM_NOT_FOUND));
            return;
        } else if (items.size() > 1) {
            this.sendError(message, this.getTranslation(Translation.WATCHER_MULTIPLE_ITEM));
            return;
        }

        Item item = items.get(0);
        Watcher watcher = new Watcher();

        watcher.loadItem(item);

        if (whenIndex != -1) {
            String whenPriceType = event.getArgs().get(whenIndex + 1);

            if (!whenPriceType.equalsIgnoreCase("buy") && !whenPriceType.equalsIgnoreCase("sell")) {
                this.sendError(message, this.getTranslation(Translation.WATCHER_WRONG_TYPE));
                return;
            }

            String whenPriceValue = event.getArgs().get(priceIndex != -1 ? priceIndex + 1 : whenIndex + 2);

            if (!whenPriceValue.equalsIgnoreCase("over") && !whenPriceValue.equalsIgnoreCase("under")) {
                this.sendError(message, this.getTranslation(Translation.WATCHER_WRONG_VALUE));
                return;
            }
            float price;
            try {
                price = Float.parseFloat(event.getArgs().get(priceIndex != -1 ? priceIndex + 2 : whenIndex + 3).replace(",", "."));
            } catch (NumberFormatException e) {
                this.sendError(message, this.getTranslation(Translation.WATCHER_WRONG_PRICE));
                return;
            }

            if (whenPriceType.equalsIgnoreCase("buy") && whenPriceValue.equalsIgnoreCase("under")) {
                watcher.setWatcherType(WatcherTypes.BUY_UNDER.getId());
            } else if (whenPriceType.equalsIgnoreCase("buy") && whenPriceValue.equalsIgnoreCase("over")) {
                watcher.setWatcherType(WatcherTypes.BUY_OVER.getId());
            } else if (whenPriceType.equalsIgnoreCase("sell") && whenPriceValue.equalsIgnoreCase("under")) {
                watcher.setWatcherType(WatcherTypes.SELL_UNDER.getId());
            } else if (whenPriceType.equalsIgnoreCase("sell") && whenPriceValue.equalsIgnoreCase("over")) {
                watcher.setWatcherType(WatcherTypes.SELL_OVER.getId());
            }

            watcher.setPrice(price);
        } else {
            watcher.setWatcherType(WatcherTypes.NORMAL.getId());
        }

        if (everyIndex != -1) {
            String time = event.getArgs().get(everyIndex + 1);
            long parsedTime = TimeConverter.fromString(time);
            watcher.setRepeatEvery(parsedTime);
        }

        watcher.setUser(this.getDiscordUser(event));
        wr.save(watcher);

        this.sendInfo(message, this.getTranslation(Translation.WATCHER_ADDED));
    }
}
