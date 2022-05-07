package xo.marketbot.services;

import fr.alexpado.xodb4j.XoDB;
import fr.alexpado.xodb4j.interfaces.IItem;
import io.sentry.Sentry;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.PrivateChannel;
import net.dv8tion.jda.api.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.repositories.WatcherRepository;
import xo.marketbot.responses.EntityDisplay;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;
import xo.marketbot.tools.Utilities;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class WatcherTask implements Runnable {

    private final static Logger             LOGGER = LoggerFactory.getLogger(WatcherTask.class);
    private final        TranslationService translationService;
    private final        WatcherRepository  repository;
    private final        JdaStore           store;
    private final        XoDB               xoDB;

    public WatcherTask(TranslationService translationService, WatcherRepository repository, JdaStore store, XoDB xoDB) {

        this.translationService = translationService;
        this.repository         = repository;
        this.store              = store;
        this.xoDB               = xoDB;
    }

    @Override
    @Scheduled(cron = "0/1 * * * * *")
    public void run() {

        Optional<JDA> optionalJda = this.store.getJda();

        if (optionalJda.isEmpty()) {
            LOGGER.warn("JDA unavailable.");
            return;
        }

        JDA jda = optionalJda.get();

        LocalDateTime timeReference = LocalDateTime.now();
        List<Watcher> watchers      = this.repository.findAllPotentials();

        for (Watcher watcher : watchers) {
            if (watcher.getLastExecution().plusSeconds(watcher.getTiming()).isBefore(timeReference)) {
                TranslationContext context = this.translationService.getContext(watcher.getOwner().getLanguage());
                try {
                    IItem item = this.xoDB.items().findById(watcher.getItemId()).complete();

                    boolean maySend = switch (watcher.getTrigger()) {
                        case EVERYTIME -> true;
                        case BUY_OVER -> item.getMarketBuy() > watcher.getPriceReference();
                        case BUY_UNDER -> item.getMarketBuy() < watcher.getPriceReference();
                        case SELL_OVER -> item.getMarketSell() > watcher.getPriceReference();
                        case SELL_UNDER -> item.getMarketSell() < watcher.getPriceReference();
                    };

                    if (maySend) {
                        EmbedBuilder builder = new EntityDisplay(context, jda, this.xoDB, watcher, item);
                        // Retrieve the user
                        User           user    = jda.retrieveUserById(watcher.getOwner().getId()).complete();
                        PrivateChannel channel = user.openPrivateChannel().complete();
                        channel.sendMessageEmbeds(builder.build()).complete();

                        watcher.refresh(item);
                    }

                    watcher.setLastExecution(Utilities.toNormalizedDateTime(LocalDateTime.now(), watcher.getTiming()));
                    break; // Only execute one every 3 seconds
                } catch (Exception e) {
                    watcher.setFailureCount(watcher.getFailureCount() + 1);
                    LOGGER.error("Unable to execute watcher {}", watcher.getId(), e);
                    Sentry.captureException(e);
                } finally {
                    this.repository.save(watcher);
                }
            }
        }
    }

}
