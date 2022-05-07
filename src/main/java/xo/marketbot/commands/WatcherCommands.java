package xo.marketbot.commands;

import fr.alexpado.jda.interactions.annotations.Choice;
import fr.alexpado.jda.interactions.annotations.Interact;
import fr.alexpado.jda.interactions.annotations.Option;
import fr.alexpado.jda.interactions.annotations.Param;
import fr.alexpado.jda.interactions.responses.SlashResponse;
import fr.alexpado.xodb4j.XoDB;
import fr.alexpado.xodb4j.interfaces.IItem;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.entities.interfaces.common.Fieldable;
import xo.marketbot.enums.WatcherTrigger;
import xo.marketbot.repositories.WatcherRepository;
import xo.marketbot.responses.EntitiesDisplay;
import xo.marketbot.responses.SimpleMessageEmbed;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;
import xo.marketbot.services.interactions.InteractionBean;
import xo.marketbot.services.interactions.pagination.PaginationTarget;
import xo.marketbot.services.interactions.responses.SimpleSlashResponse;
import xo.marketbot.tools.TimeConverter;

import java.awt.*;
import java.util.List;
import java.util.Optional;

import static xo.marketbot.services.i18n.TranslationService.*;

@InteractionBean
@Service
public class WatcherCommands {

    private static final Logger             LOGGER = LoggerFactory.getLogger(SearchCommands.class);
    private final        XoDB               xoDB;
    private final        TranslationService translationService;
    private final        WatcherRepository  watcherRepository;

    public WatcherCommands(XoDB xoDB, TranslationService translationService, WatcherRepository watcherRepository) {

        this.xoDB               = xoDB;
        this.translationService = translationService;
        this.watcherRepository  = watcherRepository;
    }

    @Interact(name = "watchers/create", description = "Create a watcher (message in DMs when price for a given item changes)", options = {@Option(name = "item", description = "The item name you want to watch", required = true, type = OptionType.STRING, autoComplete = true), @Option(name = "trigger", description = "In which case the watcher message should be sent ?", required = true, type = OptionType.STRING, choices = {@Choice(id = "sell_over", display = "When the sell price is over"), @Choice(id = "sell_under", display = "When the sell price is under"), @Choice(id = "buy_over", display = "When the buy price is over"), @Choice(id = "buy_under", display = "When the buy price is under"), @Choice(id = "everytime", display = "Everytime the price changes")}), @Option(name = "price", description = "The price limit you want to set (may not be used depending on the trigger)", type = OptionType.STRING), @Option(name = "frequency", description = "How often the price should be checked", type = OptionType.STRING)}, defer = true)
    public SlashResponse createWatcher(JDA jda, UserEntity user, ChannelEntity channel, @Param("item") String itemId, @Param("trigger") String triggerName, @Param("price") String priceParam, @Param("frequency") String frequencyParam) throws Exception {

        TranslationContext context     = this.translationService.getContext(channel.getEffectiveLanguage());
        TranslationContext userContext = this.translationService.getContext(user.getLanguage());

        // Doing checks right here allow to not send an HTTP request if we already know that the command will fail.

        WatcherTrigger trigger = Optional.ofNullable(triggerName).map(WatcherTrigger::from)
                .orElse(WatcherTrigger.EVERYTIME);
        Double price  = Optional.ofNullable(priceParam).map(Double::parseDouble).orElse(null);
        long   timing = TimeConverter.fromString(Optional.ofNullable(frequencyParam).orElse("5m")) / 1000;

        if (timing < 300) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__INVALID_PARAM__FREQUENCY));
        }

        if (trigger.isRequiringPrice() && price == null) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__INVALID_PARAM__PRICE_TRIGGER));
        }

        if (price != null && price < 0d) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__INVALID_PARAM__PRICE));
        }

        int   id   = Integer.parseInt(itemId);
        IItem item = this.xoDB.items().findById(id).complete();

        Watcher watcher = new Watcher(userContext, user, item, trigger, price, timing, true);

        watcher = this.watcherRepository.save(watcher);
        return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_WATCHER__CREATED, watcher.getId()));
    }

    @Interact(name = "watchers/list", description = "List your watchers")
    public Object listWatchers(UserEntity user, ChannelEntity channel, JDA jda) {

        TranslationContext context  = this.translationService.getContext(channel.getEffectiveLanguage());
        List<Watcher>      watchers = this.watcherRepository.findAllByOwnerId(user.getId());

        if (watchers.isEmpty()) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_WATCHER__LIST_EMPTY));
        }

        return new PaginationTarget(new EntitiesDisplay<>(context, jda, Fieldable::toField, watchers));
    }

    @Interact(name = "watchers/remove", description = "Remove a watcher", options = {@Option(name = "watcher", description = "Watcher identifier", type = OptionType.INTEGER, required = true)})
    public SlashResponse removeWatcher(UserEntity user, ChannelEntity channel, JDA jda, @Param("id") Long idParam) {

        TranslationContext context = this.translationService.getContext(channel.getEffectiveLanguage());
        // Convert long to int
        int               id              = idParam.intValue();
        Optional<Watcher> optionalWatcher = this.watcherRepository.findById(id);

        if (optionalWatcher.isEmpty()) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__NOT_FOUND));
        }

        Watcher watcher = optionalWatcher.get();

        if (!watcher.getOwner().getId().equals(user.getId())) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_WATCHER__OWNERSHIP));
        }

        this.watcherRepository.delete(watcher);
        return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_WATCHER__DELETED));
    }

    @Interact(name = "watchers/settings", description = "Change a watcher settings", options = {@Option(name = "watcher", description = "Watcher identifier", required = true, type = OptionType.NUMBER), @Option(name = "trigger", description = "In which case the watcher should be sent ?", type = OptionType.STRING, choices = {@Choice(id = "sell_over", display = "When the market sell price is over"), @Choice(id = "sell_under", display = "When the market sell price is under"), @Choice(id = "buy_over", display = "When the market buy price is over"), @Choice(id = "buy_under", display = "When the market buy price is under"), @Choice(id = "everytime", display = "Everytime the price changes")}), @Option(name = "price", description = "The price limit you want to set (may not be used depending on the trigger)", type = OptionType.STRING), @Option(name = "frequency", description = "How often the price should be checked", type = OptionType.STRING)})
    public SlashResponse changeWatcherBehavior(JDA jda, UserEntity user, ChannelEntity channel, @Param("watcher") Long watcherId, @Param("trigger") String triggerName, @Param("price") String priceParam, @Param("frequency") String frequencyParam) {

        TranslationContext context         = this.translationService.getContext(channel.getEffectiveLanguage());
        Optional<Watcher>  optionalWatcher = this.watcherRepository.findById(watcherId.intValue());

        if (optionalWatcher.isEmpty() || !optionalWatcher.get().getOwner().equals(user)) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__NOT_FOUND));
        }

        Watcher watcher = optionalWatcher.get();
        Double  price   = Optional.ofNullable(priceParam).map(Double::parseDouble).orElse(null);

        if (triggerName != null) {
            WatcherTrigger trigger = WatcherTrigger.from(triggerName);
            if (trigger.isRequiringPrice() && price == null) {
                EmbedBuilder result = new EmbedBuilder().setColor(Color.RED)
                        .setDescription("Please provide a price when using the provided trigger.");
                return new SimpleSlashResponse(result);
            }

            watcher.setTrigger(trigger);
            watcher.setPriceReference(price);
        } else if (price != null) {
            watcher.setPriceReference(price);
        }

        if (frequencyParam != null) {
            long timing = TimeConverter.fromString(frequencyParam) / 1000;

            if (timing < 300) {
                return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_WATCHER__INVALID_PARAM__PRICE_TRIGGER));
            }

            watcher.setTiming(timing);
        }

        watcher.setFailureCount(0);
        this.watcherRepository.save(watcher);
        return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_WATCHER__UPDATED));
    }

    @Interact(name = "watchers/pause", description = "Pause your watchers")
    public SlashResponse pauseWatchers(ChannelEntity channel, JDA jda, UserEntity user) {

        TranslationContext context = this.translationService.getContext(channel.getEffectiveLanguage());
        if (user.isWatcherPaused()) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_WATCHER__ALREADY_PAUSED));
        }

        user.setWatcherPaused(true);
        return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_WATCHER__STATUS_PAUSED));
    }

    @Interact(name = "watchers/resume", description = "Resume your watchers")
    public SlashResponse resumeWatchers(ChannelEntity channel, JDA jda, UserEntity user) {

        TranslationContext context = this.translationService.getContext(channel.getEffectiveLanguage());
        if (!user.isWatcherPaused()) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_WATCHER__ALREADY_RESUMED));

        }

        user.setWatcherPaused(true);
        return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_WATCHER__STATUS_RESUMED));
    }

}
