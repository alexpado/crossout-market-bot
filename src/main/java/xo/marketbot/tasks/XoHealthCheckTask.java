package xo.marketbot.tasks;

import fr.alexpado.lib.rest.interfaces.IRestAction;
import fr.alexpado.xodb4j.XoDB;
import io.sentry.Sentry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xo.marketbot.services.JdaStore;

import java.net.ConnectException;
import java.util.Optional;

@Service
public class XoHealthCheckTask {

    private final XoDB     xoDB;
    private final JdaStore store;
    private       boolean  xoDbAvailable = false;

    public XoHealthCheckTask(XoDB xoDB, JdaStore store) {

        this.xoDB  = xoDB;
        this.store = store;
    }

    @Scheduled(cron = "0 0/5 * * * *") // Every 5 minutes
    public void doTask() {

        IRestAction<Void> health        = this.xoDB.health();
        boolean           statusChanged = false;

        try {
            health.complete();
            statusChanged      = !this.xoDbAvailable;
            this.xoDbAvailable = true;
        } catch (ConnectException e) {
            statusChanged      = this.xoDbAvailable;
            this.xoDbAvailable = false;
        } catch (Exception e) {
            // Keep last status, but report it just in case
            Sentry.captureException(e);
        }

        Optional<JDA> optionalJDA = this.store.getJda();
        if (statusChanged && optionalJDA.isPresent()) {
            JDA jda = optionalJDA.get();

            if (this.xoDbAvailable) {
                jda.getPresence().setPresence(OnlineStatus.ONLINE, null);
            } else {
                jda.getPresence().setPresence(
                        OnlineStatus.DO_NOT_DISTURB,
                        Activity.watching("CrossoutDB server on fire")
                );
            }
        }
    }

    public boolean isXoDbAvailable() {

        return xoDbAvailable;
    }

}
