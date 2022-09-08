package xo.marketbot.tasks;

import fr.alexpado.xodb4j.XoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class CacheTask {

    private final static Logger            LOGGER = LoggerFactory.getLogger(WatcherTask.class);
    private final        XoHealthCheckTask healthCheckTask;
    private final        XoDB              xoDB;

    public CacheTask(XoHealthCheckTask healthCheckTask, XoDB xoDB) {

        this.healthCheckTask = healthCheckTask;
        this.xoDB            = xoDB;
    }

    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void doTask() {

        try {
            if (!this.healthCheckTask.isXoDbAvailable()) {
                return;
            }

            LOGGER.info("Refreshing XoDB cache...");
            this.xoDB.buildCaches(true);
            LOGGER.info("The cache has been built.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
