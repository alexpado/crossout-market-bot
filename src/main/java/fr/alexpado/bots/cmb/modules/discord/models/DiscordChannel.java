package fr.alexpado.bots.cmb.modules.discord.models;

import lombok.Getter;
import lombok.ToString;
import net.dv8tion.jda.api.entities.GuildChannel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
@Getter
@ToString
public class DiscordChannel {

    @Id
    private long id;

    @OneToOne
    private DiscordGuild guild;

    private String name;

    public static DiscordChannel getInstance(GuildChannel channel) {
        return new DiscordChannel().refresh(channel);
    }

    public DiscordChannel refresh(GuildChannel channel) {
        this.id = channel.getIdLong();
        this.guild = DiscordGuild.getInstance(channel.getGuild());
        this.name = channel.getName();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DiscordChannel)) return false;
        DiscordChannel that = (DiscordChannel) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
