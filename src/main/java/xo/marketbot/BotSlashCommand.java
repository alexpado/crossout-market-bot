package xo.marketbot;

import fr.alexpado.jda.interactions.annotations.Choice;
import fr.alexpado.jda.interactions.annotations.Interact;
import fr.alexpado.jda.interactions.annotations.Option;
import fr.alexpado.jda.interactions.annotations.Param;
import fr.alexpado.jda.interactions.entities.responses.SimpleInteractionResponse;
import fr.alexpado.jda.interactions.enums.InteractionType;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.entities.interfaces.game.IItem;
import xo.marketbot.entities.interfaces.game.IPack;
import xo.marketbot.enums.WatcherTrigger;
import xo.marketbot.repositories.WatcherRepository;
import xo.marketbot.responses.EmptySearchEmbed;
import xo.marketbot.responses.EntitiesDisplay;
import xo.marketbot.responses.EntityDisplay;
import xo.marketbot.tools.TimeConverter;
import xo.marketbot.xodb.XoDB;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
public class BotSlashCommand {

    private static final Logger            LOGGER = LoggerFactory.getLogger(BotSlashCommand.class);
    private final        XoDB              xoDB;
    private final        WatcherRepository watcherRepository;

    public BotSlashCommand(XoDB xoDB, WatcherRepository watcherRepository) {

        this.xoDB              = xoDB;
        this.watcherRepository = watcherRepository;
    }

    @Interact(
            name = "item",
            description = "Display an item detail",
            type = InteractionType.ALL,
            options = {
                    @Option(
                            name = "name",
                            description = "Name of the item (complete or not)",
                            type = OptionType.STRING
                    ),
                    @Option(
                            name = "meta",
                            description = "Include meta items",
                            type = OptionType.BOOLEAN
                    ),
                    @Option(
                            name = "removed",
                            description = "Include removed items",
                            type = OptionType.BOOLEAN
                    )
            },
            defer = true
    )
    public InteractionResponse searchItem(JDA jda, ChannelEntity channel, @Param("name") String itemNameParam, @Param("meta") Boolean metaParam, @Param("removed") Boolean removedParam) throws Exception {

        String itemName = Optional.ofNullable(itemNameParam).orElse("");

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("query", itemName);
        searchParams.put("metaItems", metaParam != null && metaParam);
        searchParams.put("removedItems", removedParam != null && removedParam);
        searchParams.put("language", channel.getEffectiveLanguage());

        List<IItem> items         = this.xoDB.items().findAll(searchParams).complete();
        List<IItem> startingItems = items.stream().filter(item -> item.getName().toLowerCase().startsWith(itemName.toLowerCase())).toList();
        List<IItem> equalItems    = items.stream().filter(item -> item.getName().equalsIgnoreCase(itemName)).toList();

        if (equalItems.size() == 1) {
            IItem item = equalItems.get(0);
            return new SimpleInteractionResponse(new EntityDisplay(jda, this.xoDB, item));
        }

        if (startingItems.size() == 1) {
            IItem item = startingItems.get(0);
            return new SimpleInteractionResponse(new EntityDisplay(jda, this.xoDB, item));
        }

        if (!startingItems.isEmpty()) {
            return new EntitiesDisplay<>(jda, startingItems);
        }

        return new SimpleInteractionResponse(new EmptySearchEmbed(jda));
    }

    @Interact(
            name = "search/pack",
            description = "Display a pack detail",
            type = InteractionType.SLASH,
            options = {
                    @Option(
                            name = "name",
                            description = "Name of the pack (complete or not)",
                            type = OptionType.STRING
                    )
            },
            defer = true
    )
    public InteractionResponse searchPack(JDA jda, ChannelEntity channel, @Param("name") String packName) throws Exception {

        List<IPack> packs         = this.xoDB.packs().findAll().complete();
        List<IPack> startingPacks = packs.stream().filter(item -> item.getName().toLowerCase().startsWith(packName.toLowerCase())).toList();
        List<IPack> equalPacks    = packs.stream().filter(item -> item.getName().equalsIgnoreCase(packName)).toList();

        if (equalPacks.size() == 1) {
            IPack pack = equalPacks.get(0);
            return new SimpleInteractionResponse(new EntityDisplay(jda, pack));
        }

        if (startingPacks.size() == 1) {
            IPack pack = startingPacks.get(0);
            return new SimpleInteractionResponse(new EntityDisplay(jda, pack));
        }

        if (!startingPacks.isEmpty()) {
            return new EntitiesDisplay<>(jda, startingPacks);
        }

        return new SimpleInteractionResponse(new EmptySearchEmbed(jda));
    }

    @Interact(
            name = "watchers/create",
            description = "Create a watcher (notification when price for a given item changes)",
            type = InteractionType.SLASH,
            options = {
                    @Option(
                            name = "item",
                            description = "The item name you want to watch",
                            required = true,
                            type = OptionType.STRING
                    ),
                    @Option(
                            name = "trigger",
                            description = "In which case the watcher should be sent ?",
                            required = true,
                            type = OptionType.STRING,
                            choices = {
                                    @Choice(
                                            id = "sell_over",
                                            display = "When the sell price is over"
                                    ),
                                    @Choice(
                                            id = "sell_under",
                                            display = "When the sell price is under"
                                    ),
                                    @Choice(
                                            id = "buy_over",
                                            display = "When the buy price is over"
                                    ),
                                    @Choice(
                                            id = "buy_under",
                                            display = "When the buy price is under"
                                    ),
                                    @Choice(
                                            id = "everytime",
                                            display = "Everytime the price changes"
                                    )
                            }
                    ),
                    @Option(
                            name = "price",
                            description = "The price limit you want to set (may not be used depending on the trigger)",
                            type = OptionType.STRING
                    ),
                    @Option(
                            name = "frequency",
                            description = "How often the price should be checked",
                            type = OptionType.STRING
                    )
            },
            defer = true
    )
    public InteractionResponse createWatcher(JDA jda, UserEntity user, ChannelEntity channel, @Param("item") String itemName, @Param("trigger") String triggerName, @Param("price") String priceParam, @Param("frequency") String frequencyParam) throws Exception {

        // Doing checks right here allow to not send an HTTP request if we already know that the command will fail.

        WatcherTrigger trigger = Optional.ofNullable(triggerName).map(WatcherTrigger::from).orElse(WatcherTrigger.EVERYTIME);
        Double         price   = Optional.ofNullable(priceParam).map(Double::parseDouble).orElse(null);
        long           timing  = TimeConverter.fromString(Optional.ofNullable(frequencyParam).orElse("5m")) / 1000;

        if (timing < 300) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.RED).setDescription("Please provide a frequency of at least 5m.");
            return new SimpleInteractionResponse(result);
        }

        if (trigger.isRequiringPrice() && price == null) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.RED)
                                                    .setDescription("Please provide a price when using the provided trigger.");
            return new SimpleInteractionResponse(result);
        }

        if (price != null && price < 0d) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.RED).setDescription("Please provide a non-negative price.");
            return new SimpleInteractionResponse(result);
        }

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("query", itemName);
        searchParams.put("language", channel.getEffectiveLanguage());

        List<IItem> items      = this.xoDB.items().findAll(searchParams).complete();
        List<IItem> equalItems = items.stream().filter(item -> item.getName().equalsIgnoreCase(itemName)).toList();

        if (equalItems.size() > 1) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.RED).setDescription("No item.");
            return new SimpleInteractionResponse(result);
        }

        if (equalItems.isEmpty()) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.RED).setDescription("Too many items.");
            return new SimpleInteractionResponse(result);
        }

        IItem   item    = equalItems.get(0);
        Watcher watcher = new Watcher(user, item, trigger, price, timing, true);

        watcher = this.watcherRepository.save(watcher);

        EmbedBuilder result = new EmbedBuilder().setColor(Color.GREEN)
                                                .setDescription("Your watcher as been created with the id " + watcher.getId() + ".");
        return new SimpleInteractionResponse(result);
    }

    @Interact(
            name = "watchers/list",
            description = "List your watchers",
            type = InteractionType.SLASH
    )
    public InteractionResponse listWatchers(UserEntity user, ChannelEntity channel, JDA jda) {

        List<Watcher> watchers = this.watcherRepository.findAllByOwnerId(user.getId());

        if (watchers.isEmpty()) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.BLACK).setDescription("No watchers.");
            return new SimpleInteractionResponse(result);
        }

        return new EntitiesDisplay<>(jda, watchers);
    }

    @Interact(
            name = "watchers/remove",
            description = "Remove a watcher",
            type = InteractionType.SLASH,
            options = {
                    @Option(
                            name = "id",
                            description = "Watcher identifier",
                            type = OptionType.INTEGER,
                            required = true
                    )
            }
    )
    public InteractionResponse removeWatcher(UserEntity user, ChannelEntity channel, @Param("id") Long idParam) {

        // Convert long to int
        int               id              = idParam.intValue();
        Optional<Watcher> optionalWatcher = this.watcherRepository.findById(id);

        if (optionalWatcher.isEmpty()) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.ORANGE).setDescription("No watcher found.");
            return new SimpleInteractionResponse(result);
        }

        Watcher watcher = optionalWatcher.get();

        if (!watcher.getOwner().getId().equals(user.getId())) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.RED).setDescription("This is not your watcher.");
            return new SimpleInteractionResponse(result);
        }

        this.watcherRepository.delete(watcher);
        EmbedBuilder result = new EmbedBuilder().setColor(Color.GREEN).setDescription("The watcher has been removed.");
        return new SimpleInteractionResponse(result);
    }

    @Interact(
            name = "watchers/settings",
            description = "Change a watcher settings",
            type = InteractionType.SLASH,
            options = {
                    @Option(
                            name = "id",
                            description = "Watcher identifier",
                            required = true,
                            type = OptionType.NUMBER
                    ),
                    @Option(
                            name = "trigger",
                            description = "In which case the watcher should be sent ?",
                            type = OptionType.STRING,
                            choices = {
                                    @Choice(
                                            id = "sell_over",
                                            display = "When the market sell price is over"
                                    ),
                                    @Choice(
                                            id = "sell_under",
                                            display = "When the market sell price is under"
                                    ),
                                    @Choice(
                                            id = "buy_over",
                                            display = "When the market buy price is over"
                                    ),
                                    @Choice(
                                            id = "buy_under",
                                            display = "When the market buy price is under"
                                    ),
                                    @Choice(
                                            id = "everytime",
                                            display = "Everytime the price changes"
                                    )
                            }
                    ),
                    @Option(
                            name = "price",
                            description = "The price limit you want to set (may not be used depending on the trigger)",
                            type = OptionType.STRING
                    ),
                    @Option(
                            name = "frequency",
                            description = "How often the price should be checked",
                            type = OptionType.STRING
                    )
            })
    public InteractionResponse changeWatcherBehavior(JDA jda, UserEntity user, ChannelEntity channel, @Param("id") Long watcherId, @Param("trigger") String triggerName, @Param("price") String priceParam, @Param("frequency") String frequencyParam) {


        Optional<Watcher> optionalWatcher = this.watcherRepository.findById(watcherId.intValue());

        if (optionalWatcher.isEmpty() || !optionalWatcher.get().getOwner().equals(user)) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.RED).setDescription("Unable to find the requested watcher.");
            return new SimpleInteractionResponse(result);
        }

        Watcher watcher = optionalWatcher.get();
        Double  price   = Optional.ofNullable(priceParam).map(Double::parseDouble).orElse(null);

        if (triggerName != null) {
            WatcherTrigger trigger = WatcherTrigger.from(triggerName);
            if (trigger.isRequiringPrice() && price == null) {
                EmbedBuilder result = new EmbedBuilder().setColor(Color.RED)
                                                        .setDescription("Please provide a price when using the provided trigger.");
                return new SimpleInteractionResponse(result);
            }

            watcher.setTrigger(trigger);
            watcher.setPriceReference(price);
        } else if (price != null) {
            watcher.setPriceReference(price);
        }

        if (frequencyParam != null) {
            long timing = TimeConverter.fromString(frequencyParam) / 1000;

            if (timing < 300) {
                EmbedBuilder result = new EmbedBuilder().setColor(Color.RED).setDescription("Please provide a frequency of at least 5m.");
                return new SimpleInteractionResponse(result);
            }

            watcher.setTiming(timing);
        }

        watcher.setFailureCount(0);
        this.watcherRepository.save(watcher);

        EmbedBuilder result = new EmbedBuilder().setColor(Color.GREEN).setDescription("Your watcher has been updated.");
        return new SimpleInteractionResponse(result);
    }

    @Interact(
            name = "watchers/pause",
            description = "Pause your watchers",
            type = InteractionType.SLASH
    )
    public InteractionResponse pauseWatchers(JDA jda, UserEntity user) {

        if (user.isWatcherPaused()) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.ORANGE).setDescription("Your watchers are already paused.");
            return new SimpleInteractionResponse(result);
        }

        user.setWatcherPaused(true);
        EmbedBuilder result = new EmbedBuilder().setColor(Color.GREEN).setDescription("Your watchers are now resumed.");
        return new SimpleInteractionResponse(result);
    }

    @Interact(
            name = "watchers/resume",
            description = "Resume your watchers",
            type = InteractionType.SLASH
    )
    public InteractionResponse resumeWatchers(JDA jda, UserEntity user) {

        if (!user.isWatcherPaused()) {
            EmbedBuilder result = new EmbedBuilder().setColor(Color.ORANGE).setDescription("Your watchers are already running.");
            return new SimpleInteractionResponse(result);
        }

        user.setWatcherPaused(true);
        EmbedBuilder result = new EmbedBuilder().setColor(Color.GREEN).setDescription("Your watchers are now paused.");
        return new SimpleInteractionResponse(result);
    }

}
