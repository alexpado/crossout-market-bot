package xo.marketbot.services;

import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.interactions.Interaction;
import org.springframework.stereotype.Service;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.Language;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.repositories.ChannelEntityRepository;
import xo.marketbot.repositories.GuildEntityRepository;
import xo.marketbot.repositories.LanguageRepository;
import xo.marketbot.repositories.UserEntityRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class EntitySynchronization {

    private final LanguageRepository      languageRepository;
    private final GuildEntityRepository   guildRepository;
    private final ChannelEntityRepository channelRepository;
    private final UserEntityRepository    userRepository;

    public EntitySynchronization(LanguageRepository languageRepository, GuildEntityRepository guildRepository, ChannelEntityRepository channelRepository, UserEntityRepository userRepository) {

        this.languageRepository = languageRepository;
        this.guildRepository    = guildRepository;
        this.channelRepository  = channelRepository;
        this.userRepository     = userRepository;
    }

    private Language getDefaultLanguage() {

        return this.languageRepository.findById("en")
                .orElseThrow(() -> new NoSuchElementException("Unable to load default language."));
    }

    public GuildEntity mapGuild(Interaction interaction) {

        if (!interaction.isFromGuild() || interaction.getGuild() == null) {
            return null;
        }

        Optional<GuildEntity> optionalGuild = this.guildRepository.findById(interaction.getIdLong());

        GuildEntity guild;

        if (optionalGuild.isPresent()) {
            guild = optionalGuild.get();
            guild.setName(interaction.getGuild().getName());
            guild.setIcon(interaction.getGuild().getIconUrl());

        } else {
            guild = new GuildEntity(interaction.getGuild(), this.getDefaultLanguage());
        }
        return this.guildRepository.save(guild);
    }

    public ChannelEntity mapChannel(Interaction interaction) {

        if (!interaction.isFromGuild() || interaction.getChannel() == null || interaction.getChannelType() != ChannelType.TEXT) {
            return null;
        }

        Optional<ChannelEntity> optionalChannel = this.channelRepository.findById(interaction.getGuildChannel()
                .getIdLong());
        ChannelEntity           channel;

        if (optionalChannel.isPresent()) {
            channel = optionalChannel.get();
            channel.setName(interaction.getGuildChannel().getName());
        } else {
            channel = new ChannelEntity(interaction.getGuildChannel(), this.mapGuild(interaction));
        }
        return this.channelRepository.save(channel);
    }

    public UserEntity mapUser(Interaction interaction) {

        Optional<UserEntity> optionalUser = this.userRepository.findById(interaction.getUser().getIdLong());
        UserEntity           user;

        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            user.setUsername(interaction.getUser().getName());
            user.setDiscriminator(interaction.getUser().getDiscriminator());
            user.setAvatar(interaction.getUser().getAvatarUrl());
        } else {
            user = new UserEntity(interaction.getUser(), this.getDefaultLanguage());
        }
        return this.userRepository.save(user);
    }

}
