package xo.marketbot;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import xo.marketbot.configurations.interfaces.IDiscordConfiguration;
import xo.marketbot.services.EntityUpdater;
import xo.marketbot.services.JdaStore;
import xo.marketbot.services.interactions.InteractionWrapper;

import javax.security.auth.login.LoginException;

@SpringBootApplication
@EnableScheduling
public class XoMarketApplication extends ListenerAdapter {

    private static final Logger             LOGGER = LoggerFactory.getLogger(XoMarketApplication.class);
    private final        InteractionWrapper wrapper;

    public XoMarketApplication(IDiscordConfiguration configuration, JdaStore store, InteractionWrapper wrapper, EntityUpdater entityUpdater) throws LoginException {

        this.wrapper = wrapper;

        if (!configuration.getToken().isEmpty() && configuration.isEnabled()) {
            JDABuilder builder = JDABuilder.createLight(configuration.getToken());

            builder.addEventListeners(this);
            builder.addEventListeners(entityUpdater);
            builder.addEventListeners(store);
            builder.build();
        }
    }

    public static void main(String[] args) {

        SpringApplication.run(XoMarketApplication.class, args);
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        LOGGER.debug("- Current Login Information");
        LOGGER.debug("     Account ID: {}", event.getJDA().getSelfUser().getIdLong());
        LOGGER.debug("   Account Name: {}", event.getJDA().getSelfUser().getName());

        LOGGER.debug("Updating commands...");
        this.wrapper.hook(event.getJDA());
    }

}
