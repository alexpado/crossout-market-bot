package fr.alexpado.bots.cmb.schedulers;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.api.HealthEndpoint;
import fr.alexpado.bots.cmb.bot.DiscordBot;
import fr.alexpado.bots.cmb.interfaces.APIEndpoint;
import fr.alexpado.bots.cmb.modules.crossout.models.db.HealthStat;
import fr.alexpado.bots.cmb.modules.crossout.repositories.OldWatcherRepository;
import fr.alexpado.bots.cmb.modules.crossout.repositories.db.HealthStatRepository;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Component
@Transactional
public class TickerTask {

    private final CrossoutConfiguration configuration;
    private final HealthStatRepository  repository;
    private final OldWatcherRepository  watcherRepository;
    private       int                   checkTimeout;
    private       int                   bannerIndex;

    public TickerTask(CrossoutConfiguration configuration, HealthStatRepository repository, OldWatcherRepository watcherRepository) {

        this.configuration     = configuration;
        this.repository        = repository;
        this.watcherRepository = watcherRepository;

        this.bannerIndex  = 0;
        this.checkTimeout = 0;
    }

    @Scheduled(cron = "0 0/1 * 1/1 * ?")
    public void doXoDbHealthCheck() {

        APIEndpoint<HealthStat, Void> endpoint = new HealthEndpoint(this.configuration.getApiHost());
        HealthStat                    stat     = endpoint.getOne(null).orElse(HealthStat.create(-1));

        this.repository.save(stat);

        // Cleanup
        LocalDateTime time    = LocalDateTime.now();
        LocalDateTime history = time.minusWeeks(1);

        this.repository.deleteAllByRunAtBefore(history);

        // Update JDA Status
        if (!stat.isAvailable() && DiscordBot.jda != null) {

            DiscordBot.jda.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        } else if (stat.isAvailable() && DiscordBot.jda != null) {
            DiscordBot.jda.getPresence().setStatus(OnlineStatus.ONLINE);
        }
    }

    @Scheduled(cron = "0/10 * * 1/1 * ?")
    public void doTickerBannerRefresh() {

        String activityMessage = "--";


        if (DiscordBot.jda != null) {
            switch (this.bannerIndex) {
                case 0: // Guild count
                    activityMessage = String.format("%s servers.", DiscordBot.jda.getGuilds().size());
                    break;
                case 1:
                    activityMessage = String.format("%s users.", DiscordBot.jda.getUsers().size());
                    break;
                case 2:
                    activityMessage = String.format("%s watchers.", this.watcherRepository.findAll().size());
                    break;
            }

            this.bannerIndex++;

            if (this.bannerIndex > 2) {
                this.bannerIndex = 0;
            }

            DiscordBot.jda.getPresence().setActivity(Activity.of(Activity.ActivityType.DEFAULT, activityMessage));
        }

    }

}
