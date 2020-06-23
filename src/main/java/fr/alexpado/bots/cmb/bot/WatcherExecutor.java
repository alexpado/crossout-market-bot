package fr.alexpado.bots.cmb.bot;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.enums.WatcherType;
import fr.alexpado.bots.cmb.modules.crossout.models.Watcher;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Item;
import fr.alexpado.bots.cmb.modules.crossout.models.settings.UserSettings;
import fr.alexpado.bots.cmb.modules.crossout.repositories.WatcherRepository;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class WatcherExecutor {

    private static final Logger                LOGGER = LoggerFactory.getLogger(WatcherExecutor.class);
    private final        WatcherRepository     watcherRepository;
    private final        CrossoutConfiguration config;

    public WatcherExecutor(@Qualifier("crossoutConfiguration") CrossoutConfiguration config, WatcherRepository watcherRepository) {

        this.watcherRepository = watcherRepository;
        this.config            = config;
    }

    @Scheduled(fixedDelayString = "${watchers.timeout}")
    public void runWatchers() {

        if (DiscordBot.jda == null || DiscordBot.jda.getUsers().isEmpty()) {
            return; // Cancel the execution as having JDA is required.
        }

        ItemEndpoint endpoint = new ItemEndpoint(this.config);

        List<Watcher> watchers = this.watcherRepository.getExecutables(System.currentTimeMillis());
        List<Watcher> toRemove = new ArrayList<>();

        for (Watcher watcher : watchers) {
            LOGGER.debug("Executing watcher {} ...", watcher.getId());

            Optional<UserSettings> optionalUserSettings = this.config.getRepositoryAccessor()
                                                                     .getUserSettingsRepository()
                                                                     .findByUser(watcher.getUser());

            if (optionalUserSettings.isEmpty()) {
                // WUT ?!
                continue;
            }

            UserSettings   settings     = optionalUserSettings.get();
            Optional<Item> optionalItem = endpoint.getOne(watcher.getItemId(), settings.getLanguage());

            if (optionalItem.isEmpty()) {
                continue;
            }

            Item item = optionalItem.get();

            try {
                item.fetchTranslations(settings.getLanguage());

                EmbedBuilder builder = item.getDiffEmbed(DiscordBot.jda, watcher.getSellPrice(), watcher.getBuyPrice());

                User user = DiscordBot.jda.getUserById(watcher.getUser().getId());

                if (user == null) {
                    continue;
                }

                watcher.setLastExecution(System.currentTimeMillis());
                this.watcherRepository.save(watcher);

                WatcherType type = WatcherType.getFromId(watcher.getWatcherType());

                boolean send = false;

                switch (type) {
                    case SELL_OVER:
                        send = item.getBuyPrice() > watcher.getPrice();
                        break;
                    case SELL_UNDER:
                        send = item.getBuyPrice() < watcher.getPrice();
                        break;
                    case BUY_OVER:
                        send = item.getSellPrice() > watcher.getPrice();
                        break;
                    case BUY_UNDER:
                        send = item.getSellPrice() < watcher.getPrice();
                        break;
                    case NORMAL:
                        send = true;
                        break;
                }


                if (send) {
                    this.sendWatcher(user, builder, (message) -> LOGGER.debug("Watcher {} executed !", watcher.getId()), e -> {
                        LOGGER.error("An error occurred while sending the message", e);
                        LOGGER.warn("Unable to execute the watcher {} for user {}.", watcher.getId(), watcher.getUser()
                                                                                                             .getId());
                        toRemove.add(watcher);
                    });

                }
            } catch (MissingTranslationException e) {
                LOGGER.error("One or more translations are missing !", e);
            }
        }

        List<Watcher> toSave = watchers.stream()
                                       .filter(watcher -> !toRemove.contains(watcher))
                                       .collect(Collectors.toList());

        LOGGER.info("Watcher Execution Report: {} executed, {} removed.", toSave.size(), toRemove.size());

        this.watcherRepository.saveAll(toSave);
        this.watcherRepository.deleteAll(toRemove);

    }

    private void sendWatcher(User user, EmbedBuilder builder, Consumer<Message> onSuccess, Consumer<Throwable> onFailure) {

        user.openPrivateChannel()
            .queue(channel -> channel.sendMessage(builder.build()).queue(null, onFailure), onFailure);
    }

}
