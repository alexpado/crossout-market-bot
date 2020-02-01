package fr.alexpado.bots.cmb.libs.jda.events;

import fr.alexpado.bots.cmb.libs.jda.JDABot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

/**
 * @author alexpado
 * @version 0.1
 * @since 0.1
 */
public final class CommandEvent {

    private GuildMessageReceivedEvent event;
    private JDABot bot;
    private String label;
    private List<String> args;

    public CommandEvent(JDABot bot, GuildMessageReceivedEvent event, String label) {
        this.bot = bot;
        this.event = event;
        List<String> tmpArgs = Arrays.asList(event.getMessage().getContentRaw().split(" "));
        this.args = tmpArgs.subList(1, tmpArgs.size());
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    public Guild getGuild() {
        return event.getGuild();
    }

    public TextChannel getChannel() {
        return event.getChannel();
    }

    public User getAuthor() {
        return event.getAuthor();
    }

    public Member getMember() {
        return event.getMember();
    }

    public Message getMessage() {
        return event.getMessage();
    }

    public JDA getJDA() {
        return event.getJDA();
    }

    public User getSelfUser() {
        return event.getJDA().getSelfUser();
    }

    public Member getSelfMember() {
        return event.getGuild().getSelfMember();
    }

    public JDABot getBot() {
        return bot;
    }

    public List<String> getArgs() {
        return args;
    }


}
