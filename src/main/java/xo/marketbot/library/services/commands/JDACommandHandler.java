package xo.marketbot.library.services.commands;


import io.sentry.Sentry;
import io.sentry.SentryLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.JpaRepository;
import xo.marketbot.XoMarketApplication;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.entities.interfaces.common.Embeddable;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.services.commands.exceptions.SyntaxException;
import xo.marketbot.library.services.commands.impl.CommandContext;
import xo.marketbot.library.services.commands.impl.CommandEventImpl;
import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandEvent;
import xo.marketbot.library.services.commands.interfaces.ICommandHandler;
import xo.marketbot.library.services.translations.Translator;
import xo.marketbot.repositories.ChannelEntityRepository;
import xo.marketbot.repositories.GuildEntityRepository;
import xo.marketbot.repositories.UserEntityRepository;
import xo.marketbot.tools.embed.EmbedPage;

import javax.annotation.Nonnull;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Class implementing {@link ICommandHandler}.
 * <p>
 * This class is used for managing every {@link ICommand} registered by {@link IDiscordBot}.
 *
 * @author alexpado
 */
public class JDACommandHandler extends ListenerAdapter implements ICommandHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JDACommandHandler.class);

    private final Map<UUID, CommandContext> contexts;
    private final Map<String, ICommand>     commandMap;

    private final IDiscordBot discordBot;
    private final String      prefix;

    private final GuildEntityRepository   guildRepository;
    private final ChannelEntityRepository channelRepository;
    private final UserEntityRepository    userRepository;

    private final TranslationProvider translationProvider;

    /**
     * Create a new instance of this {@link ICommandHandler} implementation.
     *
     * @param discordBot
     *         The {@link IDiscordBot} which is creating this {@link ICommandHandler} implementation.
     * @param prefix
     *         The prefix to use for this {@link ICommandHandler}
     * @param guildRepository
     *         The {@link JpaRepository} allowing interaction with {@link GuildEntity}.
     * @param channelRepository
     *         The {@link JpaRepository} allowing interaction with {@link ChannelEntity}.
     * @param userRepository
     *         The {@link JpaRepository} allowing interaction with {@link UserEntity}.
     * @param translationProvider
     *
     * @throws IllegalArgumentException
     *         Threw if the provided prefix is empty.
     */
    public JDACommandHandler(@NotNull IDiscordBot discordBot, @NotNull String prefix, GuildEntityRepository guildRepository, ChannelEntityRepository channelRepository, UserEntityRepository userRepository, TranslationProvider translationProvider) {

        this.discordBot = discordBot;
        this.prefix     = prefix;

        this.guildRepository     = guildRepository;
        this.channelRepository   = channelRepository;
        this.userRepository      = userRepository;
        this.translationProvider = translationProvider;

        this.commandMap = new HashMap<>();
        this.contexts   = new HashMap<>();

        if (this.prefix.isEmpty()) {
            LOGGER.warn("Invalid prefix used: Empty prefix shouldn't be used, as it would match every single message.");
            throw new IllegalArgumentException("Unable to create CommandHandler: Invalid prefix.");
        }

        LOGGER.info("A new command handler has been created with the prefix {}.", this.prefix);
    }

    /**
     * Retrieve an {@link Optional} {@link ICommand} matching the provided label.
     *
     * @param label
     *         The label associated to the {@link ICommand} to retrieve
     *
     * @return An {@link Optional} {@link ICommand}
     */
    @Override
    @NotNull
    public Optional<ICommand> getCommand(@NotNull String label) {

        return Optional.ofNullable(this.commandMap.get(label));
    }

    /**
     * Register the provided command under the provided label.
     *
     * @param command
     *         The {@link ICommand} to register.
     *
     * @throws IllegalArgumentException
     *         Threw is the provided label is already registered.
     */
    @Override
    public void register(@NotNull ICommand command) {

        String label = command.getMeta().getLabel();

        if (this.getCommand(label).isPresent()) {
            LOGGER.warn("A module tried to register the command '{}' which is already registered.", label);
            throw new IllegalArgumentException(String.format("The `%s` command is already registered.", label));
        }
        this.commandMap.put(label, command);
    }

    /**
     * Get a {@link Map} mapping every registered command label and their corresponding {@link ICommand}.
     *
     * @return A {@link Map} of {@link String} and {@link ICommand}
     */
    @Override
    @NotNull
    public Map<String, ICommand> getCommands() {

        return this.commandMap;
    }

    /**
     * Handle the command execution from the provided {@link GuildMessageReceivedEvent}
     *
     * @param event
     *         The {@link JDA}'s {@link GuildMessageReceivedEvent}.
     */
    @Override
    public void handle(@NotNull GuildMessageReceivedEvent event) {

        String msg   = event.getMessage().getContentRaw().toLowerCase();
        String label = msg.split(" ")[0].replace(this.prefix.toLowerCase(), "");

        Optional<ICommand> optionalCommand = this.getCommand(label);

        if (optionalCommand.isEmpty()) {
            this.discordBot.onCommandNotFound(event, label);
            return;
        }

        ICommand command = optionalCommand.get();
        // Build command context
        CommandContext context;
        try {
            context = CommandContext.createContext(event, this.guildRepository, this.channelRepository, this.userRepository);
        } catch (Exception e) {
            Sentry.withScope(scope -> scope.setLevel(SentryLevel.FATAL));
            Sentry.captureException(e);

            event.getChannel().sendMessage("**Fatal Error !**\nDon't worry though, the developer has been alerted !").queue();
            return;
        }
        ICommandEvent commandEvent = new CommandEventImpl(context, label, command);

        this.discordBot.onCommandExecuted(commandEvent);

        if (!commandEvent.isCancelled()) {

            MessageBuilder messageBuilder = new MessageBuilder();

            if (XoMarketApplication.NOTICE != null) {
                messageBuilder.append(XoMarketApplication.NOTICE);
            }

            messageBuilder.setEmbed(new EmbedBuilder().setDescription(":thinking: ...").build());


            event.getChannel().sendMessage(messageBuilder.build()).queue(message -> {

                this.translationProvider.reload();

                try {
                    Object result = command.execute(context);

                    if (result instanceof EmbedBuilder) {
                        EmbedBuilder builder = (EmbedBuilder) result;
                        Translator.translate(this.translationProvider, context.getChannelEntity().getEffectiveLanguage(), builder);
                        message.editMessage(builder.build()).queue();
                    } else if (result instanceof EmbedPage) {
                        EmbedPage<?> page = (EmbedPage<?>) result;
                        Translator.translate(this.translationProvider, context.getChannelEntity().getEffectiveLanguage(), page);
                        page.setMessage(message);
                    } else if (result instanceof Embeddable) {
                        Embeddable embeddable = (Embeddable) result;
                        Translator.translate(this.translationProvider, context.getChannelEntity().getEffectiveLanguage(), embeddable);
                        message.editMessage(embeddable.toEmbed(event.getJDA()).build()).queue();
                    } else {
                        message.delete().queue();
                    }
                } catch (SyntaxException e) {

                    // TODO: Create a translatable embed.
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setColor(Color.RED);
                    builder.setDescription("Wrong syntax.");

                    message.editMessage(builder.build()).queue();

                } catch (Exception e) {
                    Exception reportableException;

                    if (e instanceof InvocationTargetException) {
                        // If it's an invocation target exception, it means that the error was most likely within the command.
                        reportableException = ((Exception) e.getCause());
                    } else {
                        reportableException = e;
                    }

                    context.reportException(command.getMeta().getLabel(), reportableException);

                    // TODO: Create a translatable embed.
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setColor(Color.RED);
                    builder.setTitle("An error occurred.");
                    builder.setDescription("An unexpected error occurred, but don't worry, the developer has been alerted !");

                    message.editMessage(builder.build()).queue();
                }

            });
        } else {
            LOGGER.debug("Command execution cancelled.");
        }
    }

    /**
     * Called when {@link JDA} catch a {@link Message} being sent in a {@link Guild}.
     *
     * @param event
     *         The {@link JDA}'s {@link GuildMessageReceivedEvent}
     */
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {

        String message = event.getMessage().getContentRaw().toLowerCase();
        if (message.startsWith(this.prefix.toLowerCase())) {
            this.handle(event);
        }
    }

    /**
     * Get the {@link IDiscordBot} associated with this {@link JDACommandHandler}.
     *
     * @return An {@link IDiscordBot} implementation instance.
     */
    @Override
    public IDiscordBot getBot() {

        return this.discordBot;
    }
}
