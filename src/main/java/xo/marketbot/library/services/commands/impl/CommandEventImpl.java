package xo.marketbot.library.services.commands.impl;

import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.library.services.commands.interfaces.ICommandEvent;
import xo.marketbot.library.services.commands.interfaces.ICommandHandler;

/**
 * Class implementing the {@link ICommandEvent}. This will be used by {@link ICommandHandler} to provide a way to the bot developer to
 * cancel a command execution if necessary (ex: A command reserved only for the developer, or a block list).
 *
 * @author alexpado
 */
public class CommandEventImpl implements ICommandEvent {

    private final ICommandContext context;
    private final String          label;
    private final ICommand        command;
    private       boolean         cancelled = false;


    public CommandEventImpl(ICommandContext context, String label, ICommand command) {

        this.context = context;
        this.label   = label;
        this.command = command;
    }

    /**
     * Check if this event has been cancelled.
     *
     * @return True if this event should be cancelled, false instead.
     */
    @Override
    public boolean isCancelled() {

        return this.cancelled;
    }

    /**
     * Define if this event should be cancelled.
     *
     * @param cancelled
     *         True if this event should be cancelled, false instead.
     */
    @Override
    public void setCancelled(boolean cancelled) {

        this.cancelled = cancelled;
    }

    /**
     * Get the {@link ICommand} that will be executed is {@link #isCancelled()} returns {@code false}.
     *
     * @return An {@link ICommand} implementation.
     */
    @Override
    public ICommand getCommand() {

        return this.command;
    }

    /**
     * Retrieve the {@link ICommandContext} for the current command's execution flow.
     *
     * @return An {@link ICommandContext}
     */
    @Override
    public ICommandContext getContext() {

        return this.context;
    }
}
