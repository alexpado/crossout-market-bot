package fr.alexpado.bots.cmb.commands;

import fr.alexpado.bots.cmb.commands.runtime.CommandRuntime;
import fr.alexpado.bots.cmb.entities.discord.ChannelEntity;
import fr.alexpado.bots.cmb.entities.discord.GuildEntity;
import fr.alexpado.bots.cmb.entities.discord.UserEntity;
import fr.alexpado.bots.cmb.entities.i18n.messages.EmptyItemListMessage;
import fr.alexpado.bots.cmb.entities.i18n.messages.FatalErrorMessage;
import fr.alexpado.bots.cmb.entities.i18n.messages.XoDBUnavailableMessage;
import fr.alexpado.bots.cmb.i18n.TranslationProvider;
import fr.alexpado.bots.cmb.interfaces.game.IItem;
import fr.alexpado.bots.cmb.repositories.ChannelEntityRepository;
import fr.alexpado.bots.cmb.repositories.GuildEntityRepository;
import fr.alexpado.bots.cmb.repositories.UserEntityRepository;
import fr.alexpado.bots.cmb.tools.Providers;
import fr.alexpado.bots.cmb.xodb.XoDB;
import fr.alexpado.jda.interfaces.IDiscordBot;
import fr.alexpado.jda.services.commands.DiscordCommand;
import fr.alexpado.jda.services.commands.annotations.Command;
import fr.alexpado.jda.services.commands.annotations.Flags;
import fr.alexpado.jda.services.commands.annotations.Param;
import fr.alexpado.jda.services.commands.interfaces.ICommand;
import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import fr.alexpado.jda.services.translations.Translator;
import fr.alexpado.lib.rest.exceptions.RestException;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Class managing the `item` command of the bot.
 * <p>
 * Usage available on the GitHub Wiki.
 *
 * @author alexpado
 */
@Component
public class ItemCommand extends DiscordCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(ItemCommand.class);

    private final XoDB                    db;
    private final TranslationProvider     translationProvider;
    private final ChannelEntityRepository channelRepository;
    private final GuildEntityRepository   guildRepository;
    private final UserEntityRepository    userRepository;

    private JDA lastJda;

    protected ItemCommand(IDiscordBot bot, XoDB xoDB, TranslationProvider translationProvider, ChannelEntityRepository channelRepository, GuildEntityRepository guildRepository, UserEntityRepository userRepository) {

        super(bot);

        this.db                  = xoDB;
        this.translationProvider = translationProvider;
        this.channelRepository   = channelRepository;
        this.guildRepository     = guildRepository;
        this.userRepository      = userRepository;

        // Register the command
        this.getBot().getCommandHandler().register("item", this);
    }

    /**
     * Execute this {@link ICommand} using data contained the provided {@link GuildMessageReceivedEvent}. If no
     * contained command matched, the {@link #getHelp()} method will be used.
     *
     * @param event
     *         The {@link GuildMessageReceivedEvent} to use.
     */
    @Override
    public void execute(@NotNull GuildMessageReceivedEvent event) {

        this.lastJda = event.getJDA();
        super.execute(event);
    }

    /**
     * This method is called by the {@link ICommandHandler} automatically.
     *
     * @param event
     *         The {@link JDA} event for the current command execution.
     * @param itemName
     *         The item name used to trigger this command.
     * @param flags
     *         All flags used when executing the command.
     */
    @Command("name...")
    public void viewItemEmbed(GuildMessageReceivedEvent event, @Param("name") String itemName, @Flags List<String> flags) {

        event.getChannel().sendMessage(new EmbedBuilder().setDescription(":thinking:").build()).queue(message -> {

            GuildEntity   guild   = Providers.provideGuild(this.guildRepository, event.getGuild());
            ChannelEntity channel = Providers.provideChannel(this.channelRepository, guild, event.getChannel());
            UserEntity    user    = Providers.provideUser(this.userRepository, event.getAuthor());

            CommandRuntime      runtime      = new CommandRuntime(guild, channel, user, message, flags);
            Map<String, Object> searchParams = this.createSearchParams(runtime, itemName);

            Consumer<List<IItem>> responseHandler  = (items) -> this.handleRequestResponse(runtime, items, itemName);
            Consumer<Throwable>   exceptionHandler = (throwable) -> this.handleRequestException(runtime, throwable);

            this.db.items().findAll(searchParams).queue(responseHandler, exceptionHandler);
        });
    }

    /**
     * This method is called by {@link #viewItemEmbed(GuildMessageReceivedEvent, String, List)} right before sending a
     * REST request to CrossoutDB. It's used to create all parameter that will be sent to execute the query.
     *
     * @param runtime
     *         The {@link CommandRuntime} to use to handle the request.
     * @param itemName
     *         The item name to use to create the query.
     *
     * @return A {@link Map} to use when sending the request to CrossoutDB.
     */
    private Map<String, Object> createSearchParams(CommandRuntime runtime, String itemName) {

        Map<String, Object> searchParams = new HashMap<>();

        if (runtime.getFlags().contains("r")) { // Are we including removed items ?
            searchParams.put("removedItems", "true");
        }

        if (runtime.getFlags().contains("m")) { // Are we including meta items ?
            searchParams.put("metaItems", "true");
        }

        searchParams.put("query", itemName);
        searchParams.put("language", runtime.getChannel().getEffectiveLanguage());

        return searchParams;
    }

    /**
     * This method is called when the CrossoutDB's response is available.
     *
     * @param runtime
     *         The {@link CommandRuntime} to use to handle the request.
     * @param items
     *         The {@link List} of {@link IItem} received from CrossoutDB's response.
     * @param itemName
     *         The item name used to execute the request.
     */
    private void handleRequestResponse(CommandRuntime runtime, List<IItem> items, String itemName) {

        Message message           = runtime.getMessage();
        JDA     jda               = message.getJDA();
        String  effectiveLanguage = runtime.getChannel().getEffectiveLanguage();

        if (items.isEmpty()) {
            try {
                message.editMessage(this.emptyItemList(jda, effectiveLanguage).build()).queue();
            } catch (IllegalAccessException e) {
                this.handleRequestException(runtime, e);
            }
        } else if (items.size() == 1) {
            message.editMessage(this.oneItemList(jda, effectiveLanguage, items.get(0)).build()).queue();
        } else {
            message.editMessage(this.multipleItemList(jda, effectiveLanguage, items, itemName).build()).queue();
        }

    }

    /**
     * This method is called when the CrossoutDB's request fails, or if an error occur while handling the response in
     * {@link #handleRequestResponse(CommandRuntime, List, String)}. It can be from different causes, meaning that this
     * method should separate them and alert the user if needed.
     *
     * @param runtime
     *         The {@link CommandRuntime} to use to handle the request.
     * @param throwable
     *         The {@link Throwable} that caused the request failure.
     */
    private void handleRequestException(CommandRuntime runtime, Throwable throwable) {

        Message message = runtime.getMessage();

        if (throwable instanceof RestException) { // If the request failed
            try {
                XoDBUnavailableMessage feedback = new XoDBUnavailableMessage(message.getJDA());
                Translator.translate(this.translationProvider, runtime.getChannel().getEffectiveLanguage(), feedback);
                message.getChannel().sendMessage(feedback.build()).queue();
                return;
            } catch (IllegalAccessException e) {
                LOGGER.error("Unable to execute a translation.", e);
            }
        }

        FatalErrorMessage fatal = new FatalErrorMessage(message.getJDA());
        message.getChannel().sendMessage(fatal.build()).queue();
    }

    /**
     * This method is called when the CrossoutDB's request returns no item to display.
     *
     * @param jda
     *         The {@link JDA} instance used for this command execution flow.
     * @param language
     *         The language to used to send a notification back to the user.
     *
     * @return A translated {@link EmbedBuilder} providing a notification about an empty result.
     *
     * @throws IllegalAccessException
     *         Throw if the {@link Translator} was unable to apply translations.
     */
    private EmbedBuilder emptyItemList(JDA jda, String language) throws IllegalAccessException {

        EmptyItemListMessage feedback = new EmptyItemListMessage(jda);
        Translator.translate(this.translationProvider, language, feedback);
        return feedback;
    }

    /**
     * This method is called when the CrossoutDB's request returns one item or if one could be matched in the result
     * list.
     *
     * @param jda
     *         The {@link JDA} instance used for this command execution flow.
     * @param language
     *         The language to used to send a notification back to the user.
     * @param item
     *         The {@link IItem} instance returned as a result of the request.
     *
     * @return A translated {@link EmbedBuilder} providing information about the {@link IItem} retrieved.
     */
    private EmbedBuilder oneItemList(JDA jda, String language, IItem item) {

        return item.toEmbed(jda, language);
    }

    /**
     * This method is called when the CrossoutDB's request returns multiple item and no one could be matched against the
     * user request.
     *
     * @param jda
     *         The {@link JDA} instance used for this command execution flow.
     * @param language
     *         The language to used to send a notification back to the user.
     * @param items
     *         A list of {@link IItem} instances returned as request results.
     * @param itemName
     *         The The item name used to create the query.
     *
     * @return A translated {@link EmbedBuilder} displaying all {@link IItem}'s names retrieved.
     */
    private EmbedBuilder multipleItemList(JDA jda, String language, List<IItem> items, String itemName) {

        Optional<IItem> optionalItem = items.stream().filter(i -> i.getName().equalsIgnoreCase(itemName)).findFirst();
        return optionalItem.map(item -> this.oneItemList(jda, language, item)).orElse(null);
    }

    /**
     * Retrieves a {@link MessageEmbed} containing all information about how to use this {@link ICommand}.
     *
     * @return A {@link MessageEmbed} instance, or null.
     */
    @Override
    public @Nullable MessageEmbed getHelp() {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Work in progres...");

        return builder.build();
    }
}
