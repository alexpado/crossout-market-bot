package fr.alexpado.bots.cmb.interfaces;

import fr.alexpado.bots.cmb.enums.WatcherTypes;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.repositories.WatcherRepository;
import fr.alexpado.bots.cmb.tools.TimeConverter;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class AbstractWatcherCommand extends BotCommand {

    public AbstractWatcherCommand(JDAModule module, String label) {
        super(module, label);
    }

    @Override
    public List<String> getLanguageKeys() {
        return Arrays.asList(
                Translation.WATCHERS_WRONG_TYPE,
                Translation.WATCHERS_WRONG_VALUE,
                Translation.WATCHERS_WRONG_PRICE
        );
    }

    protected WatcherRepository getRepository() {
        return this.getConfig().watcherRepository;
    }

    protected Optional<Watcher> getWatcher(Message message, DiscordUser user, int id) {
        WatcherRepository wr = this.getConfig().watcherRepository;
        Optional<Watcher> optionalWatcher = wr.findById(id);

        if (!optionalWatcher.isPresent()) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_NOTFOUND));
            return Optional.empty();
        }

        Watcher watcher = optionalWatcher.get();

        if (watcher.getUser().getId() != user.getId()) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_FORBIDDEN));
            return Optional.empty();
        }

        return optionalWatcher;
    }


    protected boolean updateWatcher(CommandEvent event, Watcher watcher, Message message, boolean update) {
        int whenIndex = event.getArgs().indexOf("when");
        int priceIndex = event.getArgs().indexOf("price");
        int everyIndex = event.getArgs().indexOf("every");
        int alwaysIndex = event.getArgs().indexOf("always");

        if (whenIndex != -1) {
            String whenPriceType = event.getArgs().get(whenIndex + 1);

            if (!whenPriceType.equalsIgnoreCase("buy") && !whenPriceType.equalsIgnoreCase("sell")) {
                this.sendError(message, this.getTranslation(Translation.WATCHERS_WRONG_TYPE));
                return false;
            }

            String whenPriceValue = event.getArgs().get(priceIndex != -1 ? priceIndex + 1 : whenIndex + 2);

            if (!whenPriceValue.equalsIgnoreCase("over") && !whenPriceValue.equalsIgnoreCase("under")) {
                this.sendError(message, this.getTranslation(Translation.WATCHERS_WRONG_VALUE));
                return false;
            }
            float price;
            try {
                price = Float.parseFloat(event.getArgs().get(priceIndex != -1 ? priceIndex + 2 : whenIndex + 3).replace(",", "."));
            } catch (NumberFormatException e) {
                this.sendError(message, this.getTranslation(Translation.WATCHERS_WRONG_PRICE));
                return false;
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
            if (!update || alwaysIndex != -1) {
                watcher.setWatcherType(WatcherTypes.NORMAL.getId());
            }
        }

        if (everyIndex != -1) {
            String time = event.getArgs().get(everyIndex + 1);
            long parsedTime = TimeConverter.fromString(time);
            watcher.setRepeatEvery(parsedTime);
        }

        watcher.setUser(this.getDiscordUser(event));
        this.getConfig().watcherRepository.save(watcher);

        return true;
    }
}
