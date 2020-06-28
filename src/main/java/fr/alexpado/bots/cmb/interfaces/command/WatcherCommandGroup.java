package fr.alexpado.bots.cmb.interfaces.command;

import fr.alexpado.bots.cmb.enums.WatcherType;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.modules.crossout.models.Watcher;
import fr.alexpado.bots.cmb.modules.crossout.repositories.WatcherRepository;
import fr.alexpado.bots.cmb.tools.TimeConverter;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class WatcherCommandGroup extends TranslatableBotCommand {

    public WatcherCommandGroup(JDAModule module, String label) {
        super(module, label);
    }

    public final Optional<Long> getTime(CommandEvent event) {
        int everyIndex = event.getArgs().indexOf("every");
        int always = event.getArgs().indexOf("always");

        if (everyIndex != -1) {
            String time = event.getArgs().get(everyIndex + 1);
            long parsedTime = TimeConverter.fromString(time);
            return Optional.of(parsedTime);
        } else if (always != -1) {
            // Always stick to the default duration
            return Optional.of(this.getConfig().getWatchersTimeout());
        }

        return Optional.empty();
    }

    public final Optional<Float> getPrice(CommandEvent event) throws NumberFormatException {
        int whenIndex = event.getArgs().indexOf("when");
        int priceIndex = event.getArgs().indexOf("price");

        if (priceIndex != -1) {
            float price = Float.parseFloat(event.getArgs().get(priceIndex + 2));
            return Optional.of(price);
        }

        if (whenIndex != -1) {
            float price = Float.parseFloat(event.getArgs().get(whenIndex + 3));
            return Optional.of(price);
        }

        return Optional.empty();
    }

    public final Optional<WatcherType> getType(CommandEvent event) {
        int whenIndex = event.getArgs().indexOf("when");
        int priceIndex = event.getArgs().indexOf("price");

        if (whenIndex == -1) {
            return Optional.empty();
        }

        String whenPriceType = event.getArgs().get(whenIndex + 1);
        String whenPriceValue = event.getArgs().get(priceIndex != -1 ? priceIndex + 1 : whenIndex + 2);

        if (whenPriceType.equalsIgnoreCase("buy") && whenPriceValue.equalsIgnoreCase("under")) {
            return Optional.of(WatcherType.BUY_UNDER);
        } else if (whenPriceType.equalsIgnoreCase("buy") && whenPriceValue.equalsIgnoreCase("over")) {
            return Optional.of(WatcherType.BUY_OVER);
        } else if (whenPriceType.equalsIgnoreCase("sell") && whenPriceValue.equalsIgnoreCase("under")) {
            return Optional.of(WatcherType.SELL_UNDER);
        } else if (whenPriceType.equalsIgnoreCase("sell") && whenPriceValue.equalsIgnoreCase("over")) {
            return Optional.of(WatcherType.SELL_OVER);
        }

        return Optional.of(WatcherType.UNKNOWN);
    }

    public final Optional<String> getItemName(CommandEvent event) {
        int whenIndex = event.getArgs().indexOf("when");
        int everyIndex = event.getArgs().indexOf("every");
        int forIndex = event.getArgs().indexOf("for");

        if (forIndex != -1) {
            return Optional.of(String.join(" ", event.getArgs().subList(forIndex + 1, event.getArgs().size())));
        } else if (whenIndex != -1 || everyIndex != -1) {
            return Optional.empty();
        } else {
            return Optional.of(String.join(" ", event.getArgs().subList(0, event.getArgs().size())));
        }
    }

    public final Optional<Watcher> getWatcher(Message message, CommandEvent event) {
        int watcherId;

        try {
            watcherId = Integer.parseInt(event.getArgs().get(0));
        } catch (NumberFormatException e) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_WRONG_ID));
            return Optional.empty();
        }

        return this.getWatcher(message, watcherId);
    }

    public final Optional<Watcher> getWatcher(Message message, int id) {
        WatcherRepository watcherRepository = this.getConfig().getRepositoryAccessor().getWatcherRepository();
        Optional<Watcher> optionalWatcher = watcherRepository.findById(id);

        if (!optionalWatcher.isPresent()) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_NOTFOUND));
            return Optional.empty();
        }

        Watcher watcher = optionalWatcher.get();

        if (!watcher.getUser().equals(this.getDiscordUser())) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_FORBIDDEN));
            return Optional.empty();
        }

        return optionalWatcher;
    }

    public final WatcherRepository getRepository() {
        return this.getConfig().getRepositoryAccessor().getWatcherRepository();
    }

    @Override
    public List<String> getRequiredTranslation() {
        return Arrays.asList(Translation.WATCHERS_NOTFOUND, Translation.WATCHERS_FORBIDDEN, WatcherType.BUY_OVER.getTranslation(), WatcherType.BUY_UNDER
                .getTranslation(), WatcherType.SELL_OVER.getTranslation(), WatcherType.SELL_UNDER.getTranslation(), WatcherType.NORMAL
                .getTranslation(), Translation.XODB_OFFLINE);
    }

}
