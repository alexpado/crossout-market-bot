package fr.alexpado.bots.cmb.modules.discord.models;

import lombok.Getter;
import lombok.ToString;
import net.dv8tion.jda.api.entities.Guild;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@ToString
public class DiscordGuild {

    @Id
    private Long id;

    @OneToOne
    private DiscordUser owner;

    private String name;

    private String guildIcon;

    public static DiscordGuild getInstance(Guild guild) {
        return new DiscordGuild().refresh(guild);
    }

    public DiscordGuild refresh(Guild guild) {
        this.id = guild.getIdLong();
        this.owner = DiscordUser.getInstance(guild.getOwner().getUser());
        this.name = guild.getName();
        this.guildIcon = guild.getIconId();

        return this;
    }

}
