package xo.marketbot;

import fr.alexpado.jda.interactions.annotations.Choice;
import fr.alexpado.jda.interactions.annotations.Interact;
import fr.alexpado.jda.interactions.annotations.Option;
import fr.alexpado.jda.interactions.annotations.Param;
import fr.alexpado.jda.interactions.entities.responses.SimpleInteractionResponse;
import fr.alexpado.jda.interactions.enums.InteractionType;
import fr.alexpado.jda.interactions.enums.SlashTarget;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import xo.marketbot.entities.discord.*;
import xo.marketbot.entities.interfaces.game.IItem;
import xo.marketbot.entities.interfaces.game.IPack;
import xo.marketbot.enums.WatcherTrigger;
import xo.marketbot.repositories.ChannelEntityRepository;
import xo.marketbot.repositories.GuildEntityRepository;
import xo.marketbot.repositories.UserEntityRepository;
import xo.marketbot.repositories.WatcherRepository;
import xo.marketbot.responses.EntitiesDisplay;
import xo.marketbot.responses.EntityDisplay;
import xo.marketbot.responses.SimpleMessageEmbed;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;
import xo.marketbot.tools.TimeConverter;
import xo.marketbot.xodb.XoDB;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static xo.marketbot.services.i18n.TranslationService.*;

@Component
public class BotSlashCommand {

    private static final Logger                  LOGGER = LoggerFactory.getLogger(BotSlashCommand.class);
    private final        XoDB                    xoDB;
    private final        TranslationService      translationService;
    private final        WatcherRepository       watcherRepository;
    private final        UserEntityRepository    userEntityRepository;
    private final        ChannelEntityRepository channelEntityRepository;
    private final        GuildEntityRepository   guildEntityRepository;

    public BotSlashCommand(XoDB xoDB, TranslationService translationService, WatcherRepository watcherRepository, UserEntityRepository userEntityRepository, ChannelEntityRepository channelEntityRepository, GuildEntityRepository guildEntityRepository) {

        this.xoDB                    = xoDB;
        this.translationService      = translationService;
        this.watcherRepository       = watcherRepository;
        this.userEntityRepository    = userEntityRepository;
        this.channelEntityRepository = channelEntityRepository;
        this.guildEntityRepository   = guildEntityRepository;
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

        String             itemName    = Optional.ofNullable(itemNameParam).orElse("");
        TranslationContext context     = this.translationService.getContext(channel.getEffectiveLanguage());
        String             rawItemName = itemName.replaceAll("\\s\\(.*\\)$", "");

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("query", rawItemName);
        searchParams.put("metaItems", metaParam != null && metaParam);
        searchParams.put("removedItems", removedParam != null && removedParam);
        searchParams.put("language", channel.getEffectiveLanguage());

        List<IItem> items = this.xoDB.items().findAll(searchParams).complete();
        List<IItem> startingItems = items.stream()
                .filter(item -> item.getName().toLowerCase().startsWith(itemName.toLowerCase())).toList();
        List<IItem> equalItems = items.stream().filter(item -> item.getName().equalsIgnoreCase(itemName)).toList();

        if (equalItems.size() == 1) {
            IItem item = equalItems.get(0);
            return new SimpleInteractionResponse(new EntityDisplay(context, jda, this.xoDB, item));
        }

        if (startingItems.size() == 1) {
            IItem item = startingItems.get(0);
            return new SimpleInteractionResponse(new EntityDisplay(context, jda, this.xoDB, item));
        }

        if (!startingItems.isEmpty()) {
            return new EntitiesDisplay<>(context, jda, startingItems);
        }

        return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_SEARCH__EMPTY));
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

        TranslationContext context = this.translationService.getContext(channel.getEffectiveLanguage());

        List<IPack> packs = this.xoDB.packs().findAll().complete();
        List<IPack> startingPacks = packs.stream()
                .filter(item -> item.getName().toLowerCase().startsWith(packName.toLowerCase())).toList();
        List<IPack> equalPacks = packs.stream().filter(item -> item.getName().equalsIgnoreCase(packName)).toList();

        if (equalPacks.size() == 1) {
            IPack pack = equalPacks.get(0);
            return new SimpleInteractionResponse(new EntityDisplay(context, jda, pack));
        }

        if (startingPacks.size() == 1) {
            IPack pack = startingPacks.get(0);
            return new SimpleInteractionResponse(new EntityDisplay(context, jda, pack));
        }

        if (!startingPacks.isEmpty()) {
            return new EntitiesDisplay<>(context, jda, startingPacks);
        }

        return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_SEARCH__EMPTY));
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

        TranslationContext context     = this.translationService.getContext(channel.getEffectiveLanguage());
        TranslationContext userContext = this.translationService.getContext(user.getLanguage());

        // Doing checks right here allow to not send an HTTP request if we already know that the command will fail.

        WatcherTrigger trigger = Optional.ofNullable(triggerName).map(WatcherTrigger::from)
                .orElse(WatcherTrigger.EVERYTIME);
        Double price  = Optional.ofNullable(priceParam).map(Double::parseDouble).orElse(null);
        long   timing = TimeConverter.fromString(Optional.ofNullable(frequencyParam).orElse("5m")) / 1000;

        if (timing < 300) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__INVALID_PARAM__FREQUENCY));
        }

        if (trigger.isRequiringPrice() && price == null) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__INVALID_PARAM__PRICE_TRIGGER));
        }

        if (price != null && price < 0d) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__INVALID_PARAM__PRICE));
        }

        String rawItemName = itemName.replaceAll("\\s\\(.*\\)$", "");

        Map<String, Object> searchParams = new HashMap<>();
        searchParams.put("query", rawItemName);
        searchParams.put("language", channel.getEffectiveLanguage());

        List<IItem> items      = this.xoDB.items().findAll(searchParams).complete();
        List<IItem> equalItems = items.stream().filter(item -> item.getName().equalsIgnoreCase(itemName)).toList();

        if (equalItems.size() > 1) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__ITEM__TOO_MANY));
        }

        if (equalItems.isEmpty()) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__ITEM__NONE));
        }

        IItem   item    = equalItems.get(0);
        Watcher watcher = new Watcher(userContext, user, item, trigger, price, timing, true);

        watcher = this.watcherRepository.save(watcher);
        return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_WATCHER__CREATED, watcher.getId()));
    }

    @Interact(
            name = "watchers/list",
            description = "List your watchers",
            type = InteractionType.SLASH
    )
    public InteractionResponse listWatchers(UserEntity user, ChannelEntity channel, JDA jda) {

        TranslationContext context  = this.translationService.getContext(channel.getEffectiveLanguage());
        List<Watcher>      watchers = this.watcherRepository.findAllByOwnerId(user.getId());

        if (watchers.isEmpty()) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_WATCHER__LIST_EMPTY));
        }

        return new EntitiesDisplay<>(context, jda, watchers);
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
    public InteractionResponse removeWatcher(UserEntity user, ChannelEntity channel, JDA jda, @Param("id") Long idParam) {

        TranslationContext context = this.translationService.getContext(channel.getEffectiveLanguage());
        // Convert long to int
        int               id              = idParam.intValue();
        Optional<Watcher> optionalWatcher = this.watcherRepository.findById(id);

        if (optionalWatcher.isEmpty()) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__NOT_FOUND));
        }

        Watcher watcher = optionalWatcher.get();

        if (!watcher.getOwner().getId().equals(user.getId())) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_WATCHER__OWNERSHIP));
        }

        this.watcherRepository.delete(watcher);
        return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_WATCHER__DELETED));
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

        TranslationContext context         = this.translationService.getContext(channel.getEffectiveLanguage());
        Optional<Watcher>  optionalWatcher = this.watcherRepository.findById(watcherId.intValue());

        if (optionalWatcher.isEmpty() || !optionalWatcher.get().getOwner().equals(user)) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__NOT_FOUND));
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
                return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__INVALID_PARAM__PRICE_TRIGGER));
            }

            watcher.setTiming(timing);
        }

        watcher.setFailureCount(0);
        this.watcherRepository.save(watcher);
        return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_WATCHER__UPDATED));
    }

    @Interact(
            name = "watchers/pause",
            description = "Pause your watchers",
            type = InteractionType.SLASH
    )
    public InteractionResponse pauseWatchers(ChannelEntity channel, JDA jda, UserEntity user) {

        TranslationContext context = this.translationService.getContext(channel.getEffectiveLanguage());
        if (user.isWatcherPaused()) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_WATCHER__ALREADY_PAUSED));
        }

        user.setWatcherPaused(true);
        return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_WATCHER__STATUS_PAUSED));
    }

    @Interact(
            name = "watchers/resume",
            description = "Resume your watchers",
            type = InteractionType.SLASH
    )
    public InteractionResponse resumeWatchers(ChannelEntity channel, JDA jda, UserEntity user) {

        TranslationContext context = this.translationService.getContext(channel.getEffectiveLanguage());
        if (!user.isWatcherPaused()) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_WATCHER__ALREADY_RESUMED));

        }

        user.setWatcherPaused(true);
        return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_WATCHER__STATUS_RESUMED));
    }

    private Optional<InteractionResponse> applyLanguageUpdate(TranslationContext context, ChannelEntity channel, Member member, JDA jda, String languageParam, Consumer<Language> languageConsumer) {

        Optional<Language> optionalLanguage = Optional.empty();

        if (!languageParam.equals("none")) {
            optionalLanguage = this.translationService.getSupportedLanguages().stream().filter(
                    language -> language.getId().equalsIgnoreCase(languageParam)
            ).findAny();
        }

        if (!member.isOwner() && !member.hasPermission(Permission.MANAGE_PERMISSIONS) && !member.hasPermission(Permission.ADMINISTRATOR)) {
            return Optional.of(new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_GENERAL__NOT_ALLOWED)));
        }

        if (optionalLanguage.isEmpty() && !languageParam.equals("none")) {
            return Optional.of(new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_LANGUAGE__NOT_SUPPORTED)));
        }

        languageConsumer.accept(optionalLanguage.orElse(null));
        return Optional.empty();
    }

    @Interact(
            name = "language/server",
            description = "Change the language of this server",
            type = InteractionType.SLASH,
            target = SlashTarget.GUILD,
            options = {
                    @Option(
                            name = "language",
                            description = "The language",
                            type = OptionType.STRING,
                            required = true,
                            choices = {
                                    @Choice(
                                            id = "en",
                                            display = "English (EN)"
                                    ),
                                    @Choice(
                                            id = "fr",
                                            display = "Français (FR)"
                                    )
                            }
                    )
            }
    )
    public InteractionResponse changeServerLanguage(GuildEntity guild, ChannelEntity channel, Member member, JDA jda, @Param("language") String languageParam) {

        TranslationContext            context                     = this.translationService.getContext(channel.getEffectiveLanguage());
        Optional<InteractionResponse> optionalInteractionResponse = this.applyLanguageUpdate(context, channel, member, jda, languageParam, guild::setLanguage);

        if (optionalInteractionResponse.isPresent()) {
            return optionalInteractionResponse.get();
        }

        this.guildEntityRepository.save(guild);
        return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_LANGUAGE__UPDATED__GUILD));
    }

    @Interact(
            name = "language/channel",
            description = "Change the language of this channel",
            type = InteractionType.SLASH,
            target = SlashTarget.GUILD,
            options = {
                    @Option(
                            name = "language",
                            description = "The language",
                            type = OptionType.STRING,
                            required = true,
                            choices = {
                                    @Choice(
                                            id = "en",
                                            display = "English (EN)"
                                    ),
                                    @Choice(
                                            id = "fr",
                                            display = "Français (FR)"
                                    ),
                                    @Choice(
                                            id = "none",
                                            display = "Server's default"
                                    )
                            }
                    )
            }
    )
    public InteractionResponse changeChannelLanguage(ChannelEntity channel, Member member, JDA jda, @Param("language") String languageParam) {

        TranslationContext            context                     = this.translationService.getContext(channel.getEffectiveLanguage());
        Optional<InteractionResponse> optionalInteractionResponse = this.applyLanguageUpdate(context, channel, member, jda, languageParam, channel::setLanguage);

        if (optionalInteractionResponse.isPresent()) {
            return optionalInteractionResponse.get();
        }

        this.channelEntityRepository.save(channel);
        return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_LANGUAGE__UPDATED__CHANNEL));
    }

    @Interact(
            name = "language/user",
            description = "Change your own language",
            type = InteractionType.SLASH,
            target = SlashTarget.ALL,
            options = {
                    @Option(
                            name = "language",
                            description = "The language",
                            type = OptionType.STRING,
                            required = true,
                            choices = {
                                    @Choice(
                                            id = "en",
                                            display = "English (EN)"
                                    ),
                                    @Choice(
                                            id = "fr",
                                            display = "Français (FR)"
                                    )
                            }
                    )
            }
    )
    public InteractionResponse changeUserLanguage(UserEntity user, JDA jda, @Param("language") String languageParam) {

        TranslationContext context = this.translationService.getContext(user.getLanguage());

        Optional<Language> optionalLanguage = this.translationService.getSupportedLanguages().stream().filter(
                language -> language.getId().equalsIgnoreCase(languageParam)
        ).findAny();

        if (optionalLanguage.isEmpty()) {
            return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_LANGUAGE__NOT_SUPPORTED));
        }

        Language language = optionalLanguage.get();
        user.setLanguage(language);
        this.userEntityRepository.save(user);
        return new SimpleInteractionResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_LANGUAGE__UPDATED__USER));
    }

}
