package xo.marketbot.tasks;

import fr.alexpado.xodb4j.XoDB;
import fr.alexpado.xodb4j.interfaces.IItem;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xo.marketbot.configurations.interfaces.IMarketConfiguration;
import xo.marketbot.entities.discord.Language;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.repositories.WatcherRepository;
import xo.marketbot.responses.EntityDisplay;
import xo.marketbot.services.JdaStore;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WatcherTask {

    private final static Logger LOGGER = LoggerFactory.getLogger(WatcherTask.class);

    private final TranslationService   translationService;
    private final IMarketConfiguration configuration;
    private final JdaStore             store;
    private final WatcherRepository    watcherRepository;
    private final XoDB                 xoDB;

    public WatcherTask(TranslationService translationService, IMarketConfiguration configuration, JdaStore store, WatcherRepository watcherRepository, XoDB xoDB) {

        this.translationService = translationService;
        this.configuration      = configuration;
        this.store              = store;
        this.watcherRepository  = watcherRepository;
        this.xoDB               = xoDB;
    }

    @Scheduled(cron = "0 * * * * *") // Every minutes
    public void doTask() {

        Optional<JDA> jda = this.store.getJda();

        if (jda.isEmpty()) {
            return;
        }

        List<Watcher> potentials = this.watcherRepository.findAllPotentials();
        LocalDateTime now        = LocalDateTime.now().withNano(0);

        for (Watcher watcher : potentials) {

            JDA            jdaInstance = jda.get();
            User           jdaUser     = jdaInstance.retrieveUserById(watcher.getOwner().getId()).complete();
            PrivateChannel channel     = jdaUser.openPrivateChannel().complete();

            if (watcher.isLate(now)) {
                LocalDateTime from = watcher.getLastExecution();
                LOGGER.info("Watcher {} need time fix...", watcher.getId());
                while (watcher.isLate(now)) {
                    watcher.execute();
                }
                LOGGER.info("Fixed: {} -> {}", from, watcher.getLastExecution());
                this.watcherRepository.save(watcher);
            }

            if (watcher.getNextExecution().isAfter(now)) {
                LOGGER.debug("Ignore watcher {} execution (timing requirement not met)", watcher.getId());
                continue;
            }

            LOGGER.info("Executing watcher {}.", watcher.getId());

            try {
                int   itemId = watcher.getItemId();
                IItem item   = this.xoDB.items().findById(itemId).complete();

                boolean maySend = switch (watcher.getTrigger()) {
                    case SELL_UNDER -> item.getMarketSell() < watcher.getPriceReference();
                    case SELL_OVER -> item.getMarketSell() > watcher.getPriceReference();
                    case BUY_UNDER -> item.getMarketBuy() < watcher.getPriceReference();
                    case BUY_OVER -> item.getMarketBuy() > watcher.getPriceReference();
                    case EVERYTIME -> true;
                };

                Language           language = watcher.getOwner().getLanguage();
                TranslationContext context  = this.translationService.getContext(language);

                if (maySend) {
                    EmbedBuilder builder = new EntityDisplay(context, configuration, jda.get(), watcher, item);
                    try {
                        LOGGER.info("Sending watcher {} message...", watcher.getId());
                        channel.sendMessageEmbeds(builder.build()).complete();

                        watcher.refresh(item);
                        watcher.execute();
                    } catch (Exception e) {
                        LOGGER.error("An error occurred while sending the watcher message.", e);
                        Sentry.withScope(scope -> {
                            scope.setTag("watcher", watcher.getId().toString());
                            scope.setLevel(SentryLevel.INFO);
                            Sentry.captureException(e);
                        });
                        watcher.setFailureCount(watcher.getFailureCount() + 1);
                    } finally {
                        LOGGER.info("Finished handling watcher {}", watcher.getId());
                        this.watcherRepository.save(watcher);
                    }
                }
            } catch (Exception e) {
                LOGGER.error("Error handling watcher {}", watcher.getId(), e);
                Sentry.withScope(scope -> {
                    scope.setTag("watcher", watcher.getId().toString());
                    scope.setLevel(SentryLevel.WARNING);
                    Sentry.captureException(e);
                });
            }
        }
    }


}
