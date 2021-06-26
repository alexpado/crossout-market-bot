package xo.marketbot.library;


import io.sentry.Sentry;
import io.sentry.SentryLevel;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.services.commands.JDACommandHandler;
import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.library.services.commands.interfaces.ICommandEvent;
import xo.marketbot.library.services.commands.interfaces.ICommandHandler;
import xo.marketbot.repositories.ChannelEntityRepository;
import xo.marketbot.repositories.GuildEntityRepository;
import xo.marketbot.repositories.UserEntityRepository;

import javax.security.auth.login.LoginException;
import java.util.Collection;

/**
 * Class implementing {@link IDiscordBot}.
 * <p>
 * This is the main class when creating a Discord bot. You have to extend it.
 *
 * @author alexpado
 */
public abstract class DiscordBotImpl extends ListenerAdapter implements IDiscordBot {

    private static final Logger          LOGGER = LoggerFactory.getLogger(DiscordBotImpl.class);
    private final        ICommandHandler commandHandler;
    private final        JDABuilder      jdaBuilder;

    /**
     * Create a new instance of {@link DiscordBotImpl} using the provided {@link Collection} of {@link GatewayIntent} and the provided
     * prefix for the {@link ICommandHandler}.
     *
     * @param intents
     *         A {@link Collection} of {@link GatewayIntent} to use.
     * @param prefix
     *         The prefix to use when creating this {@link DiscordBotImpl}'s {@link ICommandHandler}
     * @param guildRepository
     *         The {@link JpaRepository} allowing interaction with {@link GuildEntity}.
     * @param channelRepository
     *         The {@link JpaRepository} allowing interaction with {@link ChannelEntity}.
     * @param userRepository
     *         The {@link JpaRepository} allowing interaction with {@link UserEntity}.
     */
    protected DiscordBotImpl(@NotNull Collection<GatewayIntent> intents, @NotNull String prefix, GuildEntityRepository guildRepository, ChannelEntityRepository channelRepository, UserEntityRepository userRepository, TranslationProvider provider) {

        this.jdaBuilder = JDABuilder.create(intents);
        this.jdaBuilder.addEventListeners(this);

        this.commandHandler = new JDACommandHandler(this, prefix, guildRepository, channelRepository, userRepository, provider);
        this.jdaBuilder.addEventListeners(this.commandHandler);
    }

    /**
     * Create a new instance of {@link DiscordBotImpl} using {@link GatewayIntent#DEFAULT} intents and the provided prefix for the {@link
     * ICommandHandler}.
     *
     * @param prefix
     *         The prefix to use when creating this {@link DiscordBotImpl}'s {@link ICommandHandler}
     * @param guildRepository
     *         The {@link JpaRepository} allowing interaction with {@link GuildEntity}.
     * @param channelRepository
     *         The {@link JpaRepository} allowing interaction with {@link ChannelEntity}.
     * @param userRepository
     *         The {@link JpaRepository} allowing interaction with {@link UserEntity}.
     */
    protected DiscordBotImpl(@NotNull String prefix, GuildEntityRepository guildRepository, ChannelEntityRepository channelRepository, UserEntityRepository userRepository, TranslationProvider provider) {

        this(GatewayIntent.getIntents(GatewayIntent.DEFAULT), prefix, guildRepository, channelRepository, userRepository, provider);
    }


    /**
     * Initiate the login sequence to Discord.
     *
     * @param token
     *         The token to use when login in to Discord
     */
    @Override
    public final void login(String token) {

        try {
            this.jdaBuilder.setToken(token);
            this.jdaBuilder.build();
        } catch (LoginException e) {
            LOGGER.warn("Unable to connect to Discord. The token provided is probably invalid.", e);

            Sentry.withScope(scope -> scope.setLevel(SentryLevel.FATAL));
            Sentry.captureException(e, SentryLevel.ERROR);
        }
    }

    /**
     * Retrieve the {@link ICommandHandler} in use for this {@link IDiscordBot}.
     *
     * @return An {@link ICommandHandler} implementation.
     */
    @Override
    public ICommandHandler getCommandHandler() {

        return this.commandHandler;
    }

    /**
     * Called by {@link ICommandHandler} when an {@link ICommand} matching the provided label couldn't be found.
     *
     * @param event
     *         The {@link JDA} {@link GuildMessageReceivedEvent}.
     * @param label
     *         The label that has been used to try matching an {@link ICommand}
     */
    @Override
    public void onCommandNotFound(@NotNull GuildMessageReceivedEvent event, @NotNull String label) {

        // Nothing to do
    }

    /**
     * Called by {@link ICommandHandler} when an {@link ICommand} has been matched but the user's input cannot be applied to that {@link
     * ICommand}.
     *
     * @param command
     *         The {@link ICommand} that has been matched.
     * @param context
     *         The {@link ICommandContext} that was used.
     */
    @Override
    public void onCommandSyntaxError(@NotNull ICommand command, ICommandContext context) {

        // Nothing to do
    }

    /**
     * Called by {@link ICommandHandler}
     *
     * @param event
     *         The {@link ICommandEvent} triggering this event.
     */
    @Override
    public void onCommandExecuted(@NotNull ICommandEvent event) {

        // Nothing to do
    }
}
