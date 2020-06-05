package fr.alexpado.bots.cmb.modules.crossout.models.settings;

import fr.alexpado.bots.cmb.modules.discord.models.DiscordChannel;
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
public class ChannelSettings {

    @Id
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    @MapsId
    private DiscordChannel channel;

    @Column(length = 3)
    private String language = null;

    @NotNull(message = "Missing announcement on ChannelSettings")
    private Boolean announcement = false;

    public static ChannelSettings getInstance(DiscordChannel channel) {
        ChannelSettings settings = new ChannelSettings();
        settings.setId(channel.getId());
        settings.setChannel(channel);
        return settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChannelSettings)) return false;
        ChannelSettings settings = (ChannelSettings) o;
        return id.equals(settings.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
