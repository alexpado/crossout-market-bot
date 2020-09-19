package fr.alexpado.bots.cmb.tools;

import fr.alexpado.bots.cmb.entities.discord.ChannelEntity;
import fr.alexpado.bots.cmb.entities.discord.GuildEntity;
import fr.alexpado.bots.cmb.entities.discord.UserEntity;
import fr.alexpado.bots.cmb.repositories.ChannelEntityRepository;
import fr.alexpado.bots.cmb.repositories.GuildEntityRepository;
import fr.alexpado.bots.cmb.repositories.UserEntityRepository;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

public class Providers {

    public static GuildEntity provideGuild(GuildEntityRepository repository, Guild guild) {

        return repository.findById(guild.getIdLong()).orElse(new GuildEntity(guild));
    }

    public static ChannelEntity provideChannel(ChannelEntityRepository repository, GuildEntity guild, TextChannel channel) {

        return repository.findByIdAndGuild(channel.getIdLong(), guild).orElse(new ChannelEntity(channel, guild));
    }

    public static UserEntity provideUser(UserEntityRepository repository, User user) {

        return repository.findById(user.getIdLong()).orElse(new UserEntity(user));
    }
}
