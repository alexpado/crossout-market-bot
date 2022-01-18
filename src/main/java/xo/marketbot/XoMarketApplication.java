package xo.marketbot;

import fr.alexpado.jda.interactions.entities.DispatchEvent;
import fr.alexpado.jda.interactions.interfaces.ExecutableItem;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionErrorHandler;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionItem;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionManager;
import io.sentry.Sentry;
import io.sentry.SentryLevel;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import xo.marketbot.configurations.interfaces.IDiscordConfiguration;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.repositories.WatcherRepository;
import xo.marketbot.services.EntitySynchronization;
import xo.marketbot.services.JdaStore;
import xo.marketbot.tools.Utilities;

import javax.security.auth.login.LoginException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class XoMarketApplication extends ListenerAdapter implements InteractionErrorHandler {

    public static final long   BOT_ADMIN_ID           = 149279150648066048L;
    public static final long   BOT_OFFICIAL_SERVER_ID = 508012982287073280L;
    public static final String INVITE                 = "https://discordapp.com/api/oauth2/authorize?client_id=500032551977746453&permissions=59456&scope=bot";

    private static final Logger             LOGGER = LoggerFactory.getLogger(XoMarketApplication.class);
    public static        String             NOTICE = null;
    private static       List<String>       APP_ARGS;
    private final        InteractionManager manager;

    public XoMarketApplication(IDiscordConfiguration configuration, EntitySynchronization entitySynchronization, BotSlashCommand slash, JdaStore store, WatcherRepository watcherRepository) throws LoginException {

        if (!configuration.getToken().isEmpty() && configuration.isEnabled()) {
            JDABuilder builder = JDABuilder.createLight(configuration.getToken());
            this.manager = InteractionManager.using(builder, this);
            this.manager.registerInteraction(slash);

            this.manager.registerMapping(GuildEntity.class, entitySynchronization::mapGuild);
            this.manager.registerMapping(ChannelEntity.class, entitySynchronization::mapChannel);
            this.manager.registerMapping(UserEntity.class, entitySynchronization::mapUser);

            builder.addEventListeners(this);
            builder.addEventListeners(store);
            builder.build();
        } else {
            this.manager = null;
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
                    this.manager.build(guild).queue();
                }
            }
        } else {
            LOGGER.debug("Updating commands...");
            this.manager.build(event.getJDA()).queue();
        }
    }

    /**
     * Called when an exception occurs during the execution of an {@link ExecutableItem}.
     *
     * @param event
     *         The {@link DispatchEvent} used when the error occurred.
     * @param item
     *         The {@link ExecutableItem} generating the error.
     * @param exception
     *         The {@link Exception} thrown.
     */
    @Override
    public void handleException(DispatchEvent event, ExecutableItem item, Exception exception) {

        LOGGER.warn("{}: An exception occurred", event.getPath(), exception);

        Sentry.withScope(scope -> {
            scope.setTag("interaction", event.getPath().toString());
            Sentry.captureException(exception);
        });

        if (event.getInteraction().isAcknowledged()) {
            event.getInteraction().getHook().editOriginal("An error occurred.").queue();
        } else {
            event.getInteraction().reply("An error occurred.").setEphemeral(true).queue();
        }
    }

    /**
     * Called when {@link DispatchEvent#getPath()} did not match any {@link InteractionItem}.
     *
     * @param event
     *         The unmatched {@link DispatchEvent}.
     */
    @Override
    public void handleNoAction(DispatchEvent event) {

        LOGGER.warn("Well, that's strange: {}", event.getPath());
        Sentry.withScope(scope -> {
            scope.setLevel(SentryLevel.WARNING);
            scope.setTag("interaction", event.getPath().toString());
            Sentry.captureMessage("Unknown interaction path received.");
        });

        if (event.getInteraction().isAcknowledged()) {
            event.getInteraction().getHook().editOriginal("An error occurred.").queue();
        } else {
            event.getInteraction().reply("An error occurred.").setEphemeral(true).queue();
        }
    }

    /**
     * Called when an {@link InteractionItem} has been matched but could not be executed due to its filter ({@link
     * InteractionItem#canExecute(Interaction)}.
     *
     * @param event
     *         The {@link DispatchEvent} used.
     * @param item
     *         The {@link InteractionItem} that could not be executed.
     */
    @Override
    public void handleNonExecutable(DispatchEvent event, InteractionItem item) {

        LOGGER.warn("{}: Non executable interaction.", event.getPath());

        if (event.getInteraction().isAcknowledged()) {
            event.getInteraction().getHook().editOriginal("An error occurred.").queue();
        } else {
            event.getInteraction().reply("An error occurred.").setEphemeral(true).queue();
        }
    }

}
