package fr.alexpado.bots.cmb.models.discord;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.repositories.DiscordGuildRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;
import java.util.Optional;

@Entity
@Getter
@Setter
public class DiscordGuild {

    @Id
    private long id;
    private String name;
    private String iconUrl;
    @OneToOne
    private DiscordUser user;
    @Column(length = 3)
    private String language;
    private long itemGraphInterval;

    public static DiscordGuild fromJDAGuild(AppConfig config, Guild guild, DiscordUser owner) {
        DiscordGuild discordGuild = new DiscordGuild();

        discordGuild.setId(guild.getIdLong());
        discordGuild.setName(guild.getName());
        discordGuild.setIconUrl(guild.getIconUrl());
        discordGuild.setUser(owner);
        discordGuild.setLanguage(config.getDefaultLocale());
        discordGuild.setItemGraphInterval(config.getGraphInterval());

        return discordGuild;
    }

    public static DiscordGuild fromRefresh(AppConfig config, Guild guild) {
        DiscordGuildRepository repository = config.getDiscordGuildRepository();
        DiscordUser owner = DiscordUser.fromRefresh(config, Objects.requireNonNull(guild.getOwner()).getUser());
        Optional<DiscordGuild> optionalDiscordUser = repository.findById(guild.getIdLong());
        DiscordGuild discordGuild;
        if (!optionalDiscordUser.isPresent()) {
            discordGuild = DiscordGuild.fromJDAGuild(config, guild, owner);
        } else {
            discordGuild = optionalDiscordUser.get();
            discordGuild.setName(guild.getName());
            discordGuild.setIconUrl(guild.getIconUrl());
            discordGuild.setUser(owner);
        }
        repository.save(discordGuild);
        return discordGuild;
    }

}
