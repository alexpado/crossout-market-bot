package fr.alexpado.bots.cmb.modules.crossout.models.settings;


import fr.alexpado.bots.cmb.modules.discord.models.DiscordGuild;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@Setter
@ToString
public class GuildSettings {

    @Id
    private long id;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private DiscordGuild guild;

    @Column(length = 3)
    @NotNull(message = "Missing language on GuildSetting")
    private String language = "en";

    @NotNull(message = "Missing itemGraphInterval on GuildSetting")
    private Integer itemGraphInterval = 18000;

    public static GuildSettings getInstance(DiscordGuild guild) {
        GuildSettings settings = new GuildSettings();
        settings.setId(guild.getId());
        settings.setGuild(guild);
        return settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GuildSettings)) return false;
        GuildSettings settings = (GuildSettings) o;
        return id == settings.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
