package xo.marketbot.services;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.events.guild.GuildLeaveEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateIconEvent;
import net.dv8tion.jda.api.events.guild.update.GuildUpdateNameEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateAvatarEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateDiscriminatorEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.repositories.GuildEntityRepository;
import xo.marketbot.repositories.UserEntityRepository;

import java.util.List;

@Service
public class EntityUpdater extends ListenerAdapter {

    private final GuildEntityRepository guildRepository;
    private final UserEntityRepository  userRepository;

    public EntityUpdater(GuildEntityRepository guildRepository, UserEntityRepository userRepository) {

        this.guildRepository = guildRepository;
        this.userRepository  = userRepository;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {

        List<Guild>       loadedGuilds     = event.getJDA().getGuilds();
        List<GuildEntity> registeredGuilds = this.guildRepository.findAll();
        for (GuildEntity entity : registeredGuilds) {
            entity.setActive(loadedGuilds.stream().anyMatch(guild -> guild.getIdLong() == entity.getId()));
        }

        this.guildRepository.saveAll(registeredGuilds);
    }

    @Override
    public void onGuildUpdateIcon(@NotNull GuildUpdateIconEvent event) {

        this.guildRepository.findById(event.getGuild().getIdLong()).ifPresent(guildEntity -> {
            guildEntity.setIcon(event.getNewIconUrl());
            this.guildRepository.save(guildEntity);
        });
    }

    @Override
    public void onGuildUpdateName(@NotNull GuildUpdateNameEvent event) {

        this.guildRepository.findById(event.getGuild().getIdLong()).ifPresent(guildEntity -> {
            guildEntity.setIcon(event.getNewName());
            this.guildRepository.save(guildEntity);
        });
    }

    @Override
    public void onGuildJoin(@NotNull GuildJoinEvent event) {

        this.guildRepository.findById(event.getGuild().getIdLong()).ifPresent(guildEntity -> {
            guildEntity.setActive(true);
            this.guildRepository.save(guildEntity);
        });
    }

    @Override
    public void onGuildLeave(@NotNull GuildLeaveEvent event) {

        this.guildRepository.findById(event.getGuild().getIdLong()).ifPresent(guildEntity -> {
            guildEntity.setActive(false);
            this.guildRepository.save(guildEntity);
        });
    }

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event) {

        this.userRepository.findById(event.getUser().getIdLong()).ifPresent(userEntity -> {
            userEntity.setUsername(event.getNewName());
            this.userRepository.save(userEntity);
        });
    }

    @Override
    public void onUserUpdateDiscriminator(@NotNull UserUpdateDiscriminatorEvent event) {

        this.userRepository.findById(event.getUser().getIdLong()).ifPresent(userEntity -> {
            userEntity.setDiscriminator(event.getNewDiscriminator());
            this.userRepository.save(userEntity);
        });
    }

    @Override
    public void onUserUpdateAvatar(@NotNull UserUpdateAvatarEvent event) {

        this.userRepository.findById(event.getUser().getIdLong()).ifPresent(userEntity -> {
            userEntity.setAvatar(event.getNewAvatarUrl());
            this.userRepository.save(userEntity);
        });
    }

}
