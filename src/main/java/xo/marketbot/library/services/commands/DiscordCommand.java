package xo.marketbot.library.services.commands;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.services.commands.annotations.Args;
import xo.marketbot.library.services.commands.annotations.Command;
import xo.marketbot.library.services.commands.annotations.Flags;
import xo.marketbot.library.services.commands.annotations.Param;
import xo.marketbot.library.services.commands.exceptions.SyntaxException;
import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.library.services.commands.interfaces.ICommandHandler;
import xo.marketbot.library.services.completion.CompletionServiceImpl;
import xo.marketbot.library.services.completion.interfaces.ICompletionService;
import xo.marketbot.library.services.completion.interfaces.IMatchingResult;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Class implementing {@link ICommand}.
 * <p>
 * This is the main class to use when creating a bot command. Every public method being annotated with {@link Command} will be candidates
 * for being a command.
 *
 * @author alexpado
 */
public abstract class DiscordCommand implements ICommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiscordCommand.class);

    private final IDiscordBot               bot;
    private final Map<Method, List<String>> syntaxMap;

    /**
     * Creates a new {@link DiscordCommand} instance and register it immediately.
     *
     * @param discordBot
     *         The {@link IDiscordBot} associated with this command.
     */
    protected DiscordCommand(IDiscordBot discordBot) {

        this.bot       = discordBot;
        this.syntaxMap = this.getSyntaxMap();

        this.bot.getCommandHandler().register(this);
    }

    private static List<Object> generateParameterList(IMatchingResult<Method> matchingResult, Method method, ICommandContext context) {

        Map<Class<?>, Object> paramTypeMap = new HashMap<>();

        paramTypeMap.put(User.class, context.getEvent().getAuthor());
        paramTypeMap.put(Message.class, context.getEvent().getMessage());
        paramTypeMap.put(Guild.class, context.getEvent().getGuild());
        paramTypeMap.put(Member.class, context.getEvent().getMember());
        paramTypeMap.put(TextChannel.class, context.getEvent().getChannel());
        paramTypeMap.put(MessageChannel.class, context.getEvent().getChannel());
        paramTypeMap.put(GuildChannel.class, context.getEvent().getChannel());
        paramTypeMap.put(JDA.class, context.getEvent().getJDA());
        paramTypeMap.put(GuildEntity.class, context.getGuildEntity());
        paramTypeMap.put(ChannelEntity.class, context.getChannelEntity());
        paramTypeMap.put(UserEntity.class, context.getUserEntity());
        paramTypeMap.put(GuildMessageReceivedEvent.class, context.getEvent());
        paramTypeMap.put(ICommandContext.class, context);

        List<Object> methodParameters = new ArrayList<>();

        for (Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Param.class)) {
                Param param = parameter.getAnnotation(Param.class);

                if (param.value().isEmpty()) {
                    methodParameters.add(matchingResult.getParameters());
                } else {
                    methodParameters.add(matchingResult.getParameter(param.value()));
                }

            } else if (parameter.isAnnotationPresent(Flags.class)) {
                methodParameters.add(matchingResult.getFlags());
            } else if (parameter.isAnnotationPresent(Args.class)) {
                methodParameters.add(matchingResult.getArguments());
            } else if (paramTypeMap.containsKey(parameter.getType())) {
                methodParameters.add(paramTypeMap.get(parameter.getType()));
            }
        }

        return methodParameters;
    }

    /**
     * Retrieves the {@link IDiscordBot} from which this {@link ICommand} has been register.
     *
     * @return An {@link IDiscordBot} implementation.
     */
    @Override
    @NotNull
    public final IDiscordBot getBot() {

        return this.bot;
    }

    /**
     * Called by the {@link ICommandHandler} when an {@link User} execute this command.
     *
     * @param context
     *         The {@link ICommandContext} for the current command's execution flow.
     *
     * @throws Exception
     *         Thrown if something happen during the command's execution.
     */
    @Override
    public Object execute(ICommandContext context) throws Exception {

        String       message   = context.getEvent().getMessage().getContentRaw();
        List<String> userInput = Arrays.stream(message.trim().split(" ")).filter(s -> !s.isEmpty()).collect(Collectors.toList());

        IMatchingResult<Method> matchingResult = this.getMatch(String.join(" ", userInput.subList(1, userInput.size())));
        Method                  exec           = matchingResult.getIdentifier();
        List<Object>            parameters     = DiscordCommand.generateParameterList(matchingResult, exec, context);
        return exec.invoke(this, parameters.toArray());
    }

    private ICompletionService<Method> getCompletionService() {

        return new CompletionServiceImpl<>(this.syntaxMap);
    }

    private Map<Method, List<String>> getSyntaxMap() {

        Map<Method, List<String>> map = new HashMap<>();

        for (Method declaredMethod : this.getClass().getDeclaredMethods()) {
            Command command = declaredMethod.getAnnotation(Command.class);

            if (command != null) {
                map.put(declaredMethod, Arrays.asList(command.value().split(" ")));
            }
        }

        return map;
    }

    private IMatchingResult<Method> getMatch(String userInput) {

        return this.getCompletionService()
                   .getMatchingIdentifier(userInput)
                   .orElseThrow(() -> new SyntaxException("Command not found. Please check your syntax."));
    }

}
