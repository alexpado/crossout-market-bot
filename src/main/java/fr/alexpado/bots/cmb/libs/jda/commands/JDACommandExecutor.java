package fr.alexpado.bots.cmb.libs.jda.commands;

import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import javax.annotation.Nonnull;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author alexpado
 * @version 0.1
 * @since 0.1
 */
public abstract class JDACommandExecutor {

    private final JDAModule module;
    private final String    label;

    public JDACommandExecutor(JDAModule module, String label) {

        this.module = module;
        this.label  = label;
    }

    /**
     * Gets the label that can be used to trigger the command.
     *
     * @return The command's label.
     */
    public final String getLabel() {

        return this.label;
    }

    /**
     * Gets the description for this executor.
     *
     * @return The command's description.
     */
    public String getDescription() {

        return "";
    }

    /**
     * Gets the embed to show advanced help and/or example on how to use this command.
     *
     * @return EmbedBuilder instance.
     */
    public EmbedBuilder getAdvancedHelp() {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setFooter("Advanced help is still under development. Translations won't be available.");
        return builder;
    }

    /**
     * Gets the module to which the command belong.
     *
     * @return A {@link JDAModule} instance.
     */
    public final JDAModule getModule() {

        return this.module;
    }

    /**
     * Checks if the command is enabled for a specific {@link CommandEvent}. Override it when you want to blacklist or
     * whitelist a guild or even a user from using a command.
     * <p>
     * By default, blacklist non-user executed commands.
     *
     * @param event
     *         A {@link CommandEvent} generated after a command being triggered.
     *
     * @return True if the command should be executed. False instead.
     */
    public final boolean isEnabled(CommandEvent event) {

        return !event.getAuthor().isBot();
    }

    /**
     * Gets all aliases that are able to trigger this command. If an alias declared here is used as a command label, the
     * label will have the highest priority and this command won't be executed.
     *
     * @return A list of alias to trigger this command.
     */
    @Nonnull
    public List<String> getAliases() {

        return new ArrayList<>();
    }

    public abstract void runCommand(CommandEvent event);

    private void innerEmbed(Message message, String text, Color color) {

        message.editMessage(new EmbedBuilder().setDescription(text).setColor(color).build()).queue();
    }

    /**
     * Edit the message to show an error embed.
     *
     * @param message
     *         Message to edit
     * @param text
     *         Error text to show
     */
    protected final void sendError(Message message, String text) {

        this.innerEmbed(message, text, Color.RED);
    }

    /**
     * Edit the message to show an info embed.
     *
     * @param message
     *         Message to edit
     * @param text
     *         Error text to show
     */
    protected final void sendInfo(Message message, String text) {

        this.innerEmbed(message, text, Color.CYAN);
    }

    /**
     * Edit the message to show a warning embed.
     *
     * @param message
     *         Message to edit
     * @param text
     *         Error text to show
     */
    protected final void sendWarn(Message message, String text) {

        this.innerEmbed(message, text, Color.ORANGE);
    }

    /**
     * Send a loading message and call the consumer.
     *
     * @param event
     * @param consumer
     */
    protected final void sendWaiting(CommandEvent event, Consumer<Message> consumer) {

        event.getChannel().sendMessage(new EmbedBuilder().setDescription(":thinking: ...").build()).queue(consumer);
    }


}
