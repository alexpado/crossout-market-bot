package xo.marketbot.tools;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.repositories.ChannelEntityRepository;
import xo.marketbot.repositories.GuildEntityRepository;
import xo.marketbot.repositories.UserEntityRepository;

public class Providers {

    public static GuildEntity provideGuild(GuildEntityRepository repository, Guild guild) {

        return repository.findById(guild.getIdLong()).orElse(new GuildEntity(guild));
    }

    public static ChannelEntity provideChannel(ChannelEntityRepository repository, GuildEntity guild, TextChannel channel) {

        return repository.findByIdAndGuild(channel.getIdLong(), guild).orElse(new ChannelEntity(channel, guild));
    }

    public static UserEntity provideUser(UserEntityRepository repository, net.dv8tion.jda.api.entities.User user) {

        return repository.findById(user.getIdLong()).orElse(new UserEntity(user));
    }
}
