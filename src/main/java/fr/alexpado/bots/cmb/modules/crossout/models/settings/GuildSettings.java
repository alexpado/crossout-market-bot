package fr.alexpado.bots.cmb.modules.crossout.models.settings;


import fr.alexpado.bots.cmb.modules.discord.models.DiscordGuild;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class GuildSettings {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    @MapsId
    private DiscordGuild guild;

    @Column(length = 3)
    private String language = "en";

    private Integer itemGraphInterval = 18000;

    public static GuildSettings getInstance(DiscordGuild guild) {

        GuildSettings settings = new GuildSettings();
        settings.setId(guild.getId());
        settings.setGuild(guild);
        return settings;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) { return true; }
        if (!(o instanceof GuildSettings)) { return false; }
        GuildSettings settings = (GuildSettings) o;
        return this.id.equals(settings.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.id);
    }

}
