package fr.alexpado.bots.cmb.models.discord;

import fr.alexpado.bots.cmb.bot.DiscordBot;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.Guild;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
@Getter
@Setter
public class DiscordGuild {

    public static DiscordGuild fromJDAGuild(Guild guild) {
        DiscordGuild discordGuild = new DiscordGuild();

        discordGuild.setId(guild.getIdLong());
        discordGuild.setName(guild.getName());
        discordGuild.setIconUrl(guild.getIconUrl());
        discordGuild.setUser(DiscordUser.fromJDAUser(Objects.requireNonNull(guild.getOwner()).getUser()));
        discordGuild.setLanguage(DiscordBot.getInstance().getConfig().getDefaultLocale());
        discordGuild.setItemGraphInterval(DiscordBot.getInstance().getConfig().getGraphInterval());

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
