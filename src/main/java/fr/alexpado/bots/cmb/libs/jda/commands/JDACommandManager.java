package fr.alexpado.bots.cmb.libs.jda.commands;

import fr.alexpado.bots.cmb.libs.jda.JDABot;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Class managing commands registration and execution.
 *
 * @author alexpado
 * @version 0.1
 * @since 0.1
 */
public final class JDACommandManager extends ListenerAdapter {

    private JDABot bot;
    private String prefix;
    private List<JDACommandExecutor> commands = new ArrayList<>();

    public JDACommandManager(JDABot bot, String prefix) {
        this.bot = bot;
        this.prefix = prefix;
    }

    /**
     * Retrieves the {@link JDACommandExecutor} that match the label provided. It will try to find the {@link
     * JDACommandExecutor} matching the label first. If no {@link JDACommandExecutor} has been found, it will try with
     * the aliases defined.
     *
     * @param label
     *         Label that has been used to trigger the command system.
     *
     * @return Probably a {@link JDACommandExecutor} instance if one exists matching the label provided.
     */
    private Optional<JDACommandExecutor> getCommand(String label) {
        Optional<JDACommandExecutor> optionalCommand = this.commands.stream().filter(command -> command.getLabel().equalsIgnoreCase(label)).findFirst();
        if (optionalCommand.isPresent()) {
            return optionalCommand;
        }
        return this.commands.stream().filter(command -> command.getAliases().contains(label)).findFirst();
    }

    /**
     * Registers a new {@link JDACommandExecutor}. If the executor's label is already registered, it won't be registered
     * even if the {@link JDACommandExecutor} is not the same. This would be pointless as the executor will never be
     * matched with {@link #getCommand(String)}.
     *
     * @param command
     *         The {@link JDACommandExecutor} to register.
     */
    public void registerCommand(JDACommandExecutor command) {
        if (!this.getCommand(command.getLabel()).isPresent()) {
            this.commands.add(command);
        }
    }

    /**
     * Gets every {@link JDACommandExecutor} registered.
     *
     * @return A list of {@link JDACommandExecutor}.
     */
    public List<JDACommandExecutor> getCommands() {
        return commands;
    }

    /**
     * Called when {@link #onGuildMessageReceived(GuildMessageReceivedEvent)} has detected a command. It will first try
     * to find the {@link JDACommandExecutor} linked to the label. If one is found, it will check if this executor
     * should be executed by calling {@link JDACommandExecutor#isEnabled(CommandEvent)}.
     *
     * @param event
     *         The {@link GuildMessageReceivedEvent} produced by JDA.
     *
     * @return The {@link CommandExecutionResponse} providing the execution status of the current command executor.
     */
    private CommandExecutionResponse runCommand(GuildMessageReceivedEvent event) {
        String label = event.getMessage().getContentRaw().split(" ")[0].replace(this.prefix, "");
        Optional<JDACommandExecutor> optionalCommand = this.getCommand(label);

        if (optionalCommand.isPresent()) {
            JDACommandExecutor executor = optionalCommand.get();
            CommandEvent commandEvent = new CommandEvent(this.bot, event, label);

            if (executor.isEnabled(commandEvent)) {
                executor.runCommand(commandEvent);
                this.bot.onCommandExecuted(commandEvent);
                return CommandExecutionResponse.EXECUTED;
            } else {
                return CommandExecutionResponse.DISABLED;
            }
        }
        return CommandExecutionResponse.NOTFOUND;
    }


    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
        if (event.getMessage().getContentRaw().startsWith(this.prefix)) {
            this.runCommand(event);
        }
    }

    public enum CommandExecutionResponse {
        EXECUTED,
        NOTFOUND,
        DISABLED
    }

}
