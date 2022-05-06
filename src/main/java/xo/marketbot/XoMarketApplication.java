package xo.marketbot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import xo.marketbot.configurations.interfaces.IDiscordConfiguration;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.repositories.WatcherRepository;
import xo.marketbot.services.JdaStore;
import xo.marketbot.services.interactions.InteractionWrapper;
import xo.marketbot.tools.Utilities;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class XoMarketApplication extends ListenerAdapter {

    public static final long BOT_ADMIN_ID           = 149279150648066048L;
    public static final long BOT_OFFICIAL_SERVER_ID = 508012982287073280L;

    private static final Logger             LOGGER = LoggerFactory.getLogger(XoMarketApplication.class);
    private static       List<String>       APP_ARGS;
    private final        InteractionWrapper wrapper;

    public XoMarketApplication(IDiscordConfiguration configuration, JdaStore store, WatcherRepository watcherRepository, InteractionWrapper wrapper) throws LoginException {

        this.wrapper = wrapper;

        if (!configuration.getToken().isEmpty() && configuration.isEnabled()) {
            JDABuilder builder = JDABuilder.createLight(configuration.getToken());

            builder.addEventListeners(this);
            builder.addEventListeners(store);
            builder.build();
        }

        List<Watcher> watchers = watcherRepository.findAll();

        for (Watcher watcher : watchers) {
            watcher.setLastExecution(Utilities.toNormalizedDateTime(LocalDateTime.now(), watcher.getTiming()));
        }

        watcherRepository.saveAll(watchers);
    }

    public static void main(String[] args) {

        APP_ARGS = Arrays.asList(args);
        SpringApplication.run(XoMarketApplication.class, args);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        LOGGER.debug("- Current Login Information");
        LOGGER.debug("     Account ID: {}", event.getJDA().getSelfUser().getIdLong());
        LOGGER.debug("   Account Name: {}", event.getJDA().getSelfUser().getName());

        if (APP_ARGS.contains("--dev-mode")) {
            LOGGER.warn("Running in developer mode: Only official server will benefit from SlashCommand updates.");
            LOGGER.debug("Finding guild {}", BOT_OFFICIAL_SERVER_ID);

            for (Guild guild : event.getJDA().getGuilds()) {
                if (guild.getIdLong() == BOT_OFFICIAL_SERVER_ID) {
                    LOGGER.debug("Guild found ! Updating commands...");
                    this.wrapper.hook(guild);
                }
            }
        } else {
            LOGGER.debug("Updating commands...");
            this.wrapper.hook(event.getJDA());
        }
    }

}
