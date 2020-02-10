package fr.alexpado.bots.cmb.models.discord;

import fr.alexpado.bots.cmb.bot.DiscordBot;
import fr.alexpado.bots.cmb.repositories.DiscordGuildRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Optional;

@Entity
@Getter
@Setter
public class DiscordGuild {

    public static DiscordGuild fromJDAGuild(Guild guild, DiscordUser owner) {
        DiscordGuild discordGuild = new DiscordGuild();

        discordGuild.setId(guild.getIdLong());
        discordGuild.setName(guild.getName());
        discordGuild.setIconUrl(guild.getIconUrl());
        discordGuild.setUser(owner);
        discordGuild.setLanguage(DiscordBot.getInstance().getConfig().getDefaultLocale());
        discordGuild.setItemGraphInterval(DiscordBot.getInstance().getConfig().getGraphInterval());

        return discordGuild;
    }

    public static DiscordGuild fromRefresh(DiscordGuildRepository repository, Guild guild, DiscordUser owner) {
        Optional<DiscordGuild> optionalDiscordUser = repository.findById(guild.getIdLong());
        DiscordGuild discordGuild;
        if (!optionalDiscordUser.isPresent()) {
            discordGuild = DiscordGuild.fromJDAGuild(guild, owner);
        } else {
            discordGuild = optionalDiscordUser.get();
            discordGuild.setName(guild.getName());
            discordGuild.setIconUrl(guild.getIconUrl());
            discordGuild.setUser(owner);
        }
        repository.save(discordGuild);
        return discordGuild;
    }

    @Id
    private long id;

    private String name;

    private String iconUrl;

    @OneToOne
    private DiscordUser user;

    @Column(length = 3)
    private String language;

    private long itemGraphInterval;

}
