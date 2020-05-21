package fr.alexpado.bots.cmb.modules.crossout.models.settings;

import fr.alexpado.bots.cmb.modules.discord.models.DiscordUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
public class UserSettings {

    @Id
    @Column(length = 20)
    private long id;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    private DiscordUser user;

    @Column(length = 3)
    @NotNull(message = "Missing language on UserSettings")
    private String language = "en";

    @NotNull(message = "Missing watcherPaused on UserSettings")
    private Boolean watcherPaused = false;

    public static UserSettings getInstance(DiscordUser user) {
        UserSettings settings = new UserSettings();
        settings.setId(user.getId());
        settings.setUser(user);
        return settings;
    }

    public boolean isWatcherPaused() {
        return watcherPaused;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSettings)) return false;
        UserSettings settings = (UserSettings) o;
        return id == settings.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
