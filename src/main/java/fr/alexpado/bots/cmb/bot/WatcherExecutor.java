package fr.alexpado.bots.cmb.bot;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.enums.WatcherType;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.game.Item;
import fr.alexpado.bots.cmb.repositories.TranslationRepository;
import fr.alexpado.bots.cmb.repositories.WatcherRepository;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class WatcherExecutor {

    private WatcherRepository watcherRepository;
    private TranslationRepository translationRepository;
    private AppConfig config;

    public WatcherExecutor(@Qualifier("appConfig") AppConfig config, WatcherRepository watcherRepository, TranslationRepository translationRepository) {
        this.watcherRepository = watcherRepository;
        this.translationRepository = translationRepository;
        this.config = config;
    }

    @Scheduled(fixedDelayString = "${watchers.timeout}")
    public void runWatchers() {

        if (DiscordBot.jda == null || DiscordBot.jda.getUsers().size() == 0) {
            return; // Cancel the execution as having JDA is required.
        }

        ItemEndpoint endpoint = new ItemEndpoint(this.config);

        List<Watcher> watchers = this.watcherRepository.getExecutables(System.currentTimeMillis());

        for (Watcher watcher : watchers) {
            Optional<Item> optionalItem = endpoint.getOne(watcher.getItemId(), watcher.getUser().getLanguage());

            if (!optionalItem.isPresent()) {
                continue;
            }

            Item item = optionalItem.get();

            try {
                item.fetchTranslations(watcher.getUser().getLanguage());

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
                    case UNKNOWN:
                        send = false;
                        break;
                }

                if (send) {
                    user.openPrivateChannel().queue(privateChannel ->
                            privateChannel.sendMessage(builder.build()).queue(message -> {
                                    },
                                    Throwable::printStackTrace
                            ), Throwable::printStackTrace
                    );
                }
            } catch (MissingTranslationException e) {
                e.printStackTrace();
            }
        }
    }
}
