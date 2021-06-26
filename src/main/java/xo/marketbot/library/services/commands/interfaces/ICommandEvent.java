package xo.marketbot.library.services.commands.interfaces;

import net.dv8tion.jda.api.entities.User;

/**
 * Interface representing a {@link ICommand} being executed by an {@link User}.
 *
 * @author alexpado
 */
public interface ICommandEvent {

    /**
     * Check if this event has been cancelled.
     *
     * @return True if this event should be cancelled, false instead.
     */
    boolean isCancelled();

    /**
     * Define if this event should be cancelled.
     *
     * @param cancelled
     *         True if this event should be cancelled, false instead.
     */
    void setCancelled(boolean cancelled);

    /**
     * Get the {@link ICommand} that will be executed is {@link #isCancelled()} returns {@code false}.
     *
     * @return An {@link ICommand} implementation.
     */
    ICommand getCommand();

    /**
     * Retrieve the {@link ICommandContext} for the current command's execution flow.
     *
     * @return An {@link ICommandContext}
     */
    ICommandContext getContext();

}
