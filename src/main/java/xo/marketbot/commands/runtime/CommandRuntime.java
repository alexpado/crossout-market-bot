package xo.marketbot.commands.runtime;

import net.dv8tion.jda.api.entities.Message;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.User;

import java.util.List;

public class CommandRuntime {

    private final GuildEntity   guild;
    private final ChannelEntity channel;
    private final User          user;
    private final Message       message;
    private final List<String>  flags;


    public CommandRuntime(GuildEntity guild, ChannelEntity channel, User user, Message message, List<String> flags) {

        this.guild   = guild;
        this.channel = channel;
        this.user    = user;
        this.message = message;
        this.flags   = flags;
    }

    public GuildEntity getGuild() {

        return guild;
    }

    public ChannelEntity getChannel() {

        return channel;
    }

    public User getUser() {

        return user;
    }

    public Message getMessage() {

        return message;
    }

    public List<String> getFlags() {

        return flags;
    }
}
