package xo.marketbot.library.services.commands.exceptions;

/**
 * Generic exception used when an error occurs while handling a user's command.
 *
 * @author alexpado
 */
public class CommandException extends RuntimeException {

    protected CommandException(String message) {

        super(message);
    }

}
