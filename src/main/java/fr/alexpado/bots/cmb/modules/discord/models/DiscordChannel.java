package fr.alexpado.bots.cmb.modules.discord.models;

import lombok.Getter;
import lombok.ToString;
import net.dv8tion.jda.api.entities.GuildChannel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
@ToString
public class DiscordChannel {

    @Id
    private Long id;

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

}
