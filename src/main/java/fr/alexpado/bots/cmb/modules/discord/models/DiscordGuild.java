package fr.alexpado.bots.cmb.modules.discord.models;

import lombok.Getter;
import lombok.ToString;
import net.dv8tion.jda.api.entities.Guild;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
@Getter
@ToString
public class DiscordGuild {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscordGuild)) return false;
        DiscordGuild that = (DiscordGuild) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
