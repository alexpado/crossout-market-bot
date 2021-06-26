package xo.marketbot.library.services.commands.interfaces;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.interfaces.IDiscordContext;

/**
 * Interface representing a command that can be run by any {@link User}.
 *
 * @author alexpado
 */
public interface ICommand extends IDiscordContext {

    /**
     * Retrieve the command meta for this {@link ICommand}.
     *
     * @return An {@link ICommandMeta} instance.
     */
    ICommandMeta getMeta();

    /**
     * Called by the {@link ICommandHandler} in use by the {@link IDiscordBot} when this {@link ICommand} is triggered by an {@link User}.
     * <p>
     * This method should always return it results as string or as {@link EmbedBuilder}. This will allow the {@link ICommandHandler} to
     * translate the {@link EmbedBuilder} in the correct language required.
     *
     * @param context
     *         The current {@link ICommandContext} for this execution.
     *
     * @return An {@link EmbedBuilder} containing fields to translate into the right language.
     *
     * @throws Exception
     *         Thrown when something goes wrong. This is the recommended method instead of using a big try-catch. If the {@link Exception}
     *         goes into the {@link ICommandHandler#handle(GuildMessageReceivedEvent)} method, the {@link Exception} will be reported to
     *         Sentry. You can still handle yourself some user related exceptions that should produce a user-friendly output.
     */
    Object execute(ICommandContext context) throws Exception;

}
