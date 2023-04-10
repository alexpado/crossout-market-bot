package xo.marketbot.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import xo.marketbot.helpers.CrossoutCache;

@Service
public class CacheTask {

    private final static Logger            LOGGER = LoggerFactory.getLogger(WatcherTask.class);
    private final        XoHealthCheckTask healthCheckTask;
    private final        CrossoutCache     crossoutCache;

    public CacheTask(XoHealthCheckTask healthCheckTask, CrossoutCache crossoutCache) {

        this.healthCheckTask = healthCheckTask;
        this.crossoutCache   = crossoutCache;
    }

    @Scheduled(cron = "0 0 * * * *") // Every hour
    public void doTask() {

        try {
            if (!this.healthCheckTask.isXoDbAvailable()) {
                return;
            }

            LOGGER.info("Refreshing XoDB cache...");
            this.crossoutCache.cache();
            LOGGER.info("The cache has been built.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
