package fr.alexpado.bots.cmb.modules.crossout.models.settings;


import fr.alexpado.bots.cmb.modules.discord.models.DiscordGuild;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@ToString
public class GuildSettings {

    @Id
    private Long id;

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

}
