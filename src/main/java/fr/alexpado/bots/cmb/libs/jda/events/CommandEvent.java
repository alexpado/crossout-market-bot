package fr.alexpado.bots.cmb.libs.jda.events;

import fr.alexpado.bots.cmb.libs.jda.JDABot;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author alexpado
 * @version 0.1
 * @since 0.1
 */
public final class CommandEvent {

    private final GuildMessageReceivedEvent event;
    private final JDABot                    bot;
    private final String                    label;
    private final List<String>              args;

    public CommandEvent(JDABot bot, GuildMessageReceivedEvent event, String label) {

        this.bot   = bot;
        this.event = event;
        List<String> tmpArgs = Arrays.asList(event.getMessage().getContentRaw().split(" "));
        this.args  = new ArrayList<>(tmpArgs.subList(1, tmpArgs.size()));
        this.label = label;
    }

    public String getLabel() {

        return this.label;
    }

    public Guild getGuild() {

        return this.event.getGuild();
    }

    public TextChannel getChannel() {

        return this.event.getChannel();
    }

    public User getAuthor() {

        return this.event.getAuthor();
    }

    public Member getMember() {

        return this.event.getMember();
    }

    public Message getMessage() {

        return this.event.getMessage();
    }

    public JDA getJDA() {

        return this.event.getJDA();
    }

    public User getSelfUser() {

        return this.event.getJDA().getSelfUser();
    }

    public Member getSelfMember() {

        return this.event.getGuild().getSelfMember();
    }

    public JDABot getBot() {

        return this.bot;
    }

    public List<String> getArgs() {

        return this.args;
    }


}
