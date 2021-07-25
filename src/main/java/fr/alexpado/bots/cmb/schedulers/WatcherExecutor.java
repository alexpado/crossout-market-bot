package fr.alexpado.bots.cmb.schedulers;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.bot.DiscordBot;
import fr.alexpado.bots.cmb.enums.WatcherType;
import fr.alexpado.bots.cmb.modules.crossout.models.Watcher;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Item;
import fr.alexpado.bots.cmb.modules.crossout.models.settings.UserSettings;
import fr.alexpado.bots.cmb.modules.crossout.repositories.WatcherRepository;
import fr.alexpado.bots.cmb.modules.crossout.repositories.settings.UserSettingsRepository;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class WatcherExecutor {

    private static final Logger                 LOGGER = LoggerFactory.getLogger(WatcherExecutor.class);
    private final        WatcherRepository      watcherRepository;
    private final        UserSettingsRepository userSettingsRepository;
    private final        CrossoutConfiguration  config;

    public WatcherExecutor(@Qualifier("crossoutConfiguration") CrossoutConfiguration config, WatcherRepository watcherRepository, UserSettingsRepository userSettingsRepository) {

        this.watcherRepository      = watcherRepository;
        this.config                 = config;
        this.userSettingsRepository = userSettingsRepository;
    }

    @Scheduled(fixedDelayString = "${watchers.timeout}")
    public void runWatchers() {

        if (DiscordBot.jda == null || DiscordBot.jda.getUsers().isEmpty()) {
            LOGGER.warn("No valid JDA instance found.");
            return; // Cancel the execution as having JDA is required.
        }

        ItemEndpoint endpoint = new ItemEndpoint(this.config);

        List<Watcher> watchers = this.watcherRepository.findAll();
        List<Watcher> toRemove = new ArrayList<>();
        List<Watcher> toSave   = new ArrayList<>();

        LOGGER.info("Found {} watchers in database.", watchers.size());

        for (Watcher watcher : watchers) {
            LOGGER.debug("{} | Checking watchers executable state...", watcher.getId());

            if (watcher.getLastExecution() + watcher.getRepeatEvery() > System.currentTimeMillis()) {
                LOGGER.debug("{} | The watcher is not currently executable (time)", watcher.getId());
                continue;
            }

            Optional<UserSettings> optionalSettings = this.userSettingsRepository.findByUser(watcher.getUser());

            if (optionalSettings.isEmpty()) {
                LOGGER.debug("{} | The watcher is not currently executable (missing user settings)", watcher.getId());
                continue;
            }

            if (optionalSettings.get().isWatcherPaused()) {
                LOGGER.debug("{} | The watcher is not currently executable (paused)", watcher.getId());
                continue;
            }

            LOGGER.debug("{} | The watcher is executable.", watcher.getId());

            UserSettings   settings     = optionalSettings.get();
            Optional<Item> optionalItem = endpoint.getOne(watcher.getItemId(), settings.getLanguage());

            if (optionalItem.isEmpty()) {
                LOGGER.warn("{} | Item {} not found.", watcher.getId(), watcher.getItemId());
                continue;
            }

            Item item = optionalItem.get();

            try {
                item.fetchTranslations(settings.getLanguage());

                EmbedBuilder builder = item.getDiffEmbed(DiscordBot.jda, watcher, watcher.getTranslatableWatcher(this.config, settings.getLanguage()));

                User user = DiscordBot.jda.getUserById(watcher.getUser().getId());

                if (user == null) {
                    LOGGER.warn("{} | The associated user was not found.", watcher.getId());
                    continue;
                }

                watcher.setLastExecution(System.currentTimeMillis());
                this.watcherRepository.save(watcher);

                WatcherType type = WatcherType.getFromId(watcher.getWatcherType());
                LOGGER.debug("{} | Resolved watcher type: {}", watcher.getId(), type);

                boolean send = false;

                switch (type) {
                    case SELL_OVER:
                        send = (item.getBuyPrice() / 100.0) > watcher.getPrice();
                        break;
                    case SELL_UNDER:
                        send = (item.getBuyPrice() / 100.0) < watcher.getPrice();
                        break;
                    case BUY_OVER:
                        send = (item.getSellPrice() / 100.0) > watcher.getPrice();
                        break;
                    case BUY_UNDER:
                        send = (item.getSellPrice() / 100.0) < watcher.getPrice();
                        break;
                    case NORMAL:
                        send = true;
                        break;
                }


                if (send) {

                    try {
                        LOGGER.info("{} | Sending to user {}...", watcher.getId(), user.getId());
                        this.sendWatcher(user, builder);
                        toSave.add(watcher);
                    } catch (Exception e) {
                        toRemove.add(watcher);
                        LOGGER.warn("Unable to execute the watcher {} for user {}.", watcher.getId(), watcher.getUser()
                                                                                                             .getId());
                    }

                }
            } catch (MissingTranslationException e) {
                LOGGER.error("One or more translations are missing !", e);
            } catch (Exception e) {
                LOGGER.error("Oof", e);
            }
        }

        LOGGER.info("Watcher Execution Report: {} executed, {} removed.", toSave.size(), toRemove.size());

        this.watcherRepository.saveAll(toSave);
        this.watcherRepository.deleteAll(toRemove);

    }

    private void sendWatcher(User user, EmbedBuilder builder) {

        PrivateChannel channel = user.openPrivateChannel().complete();
        channel.sendMessageEmbeds(builder.build()).complete();
    }

}
