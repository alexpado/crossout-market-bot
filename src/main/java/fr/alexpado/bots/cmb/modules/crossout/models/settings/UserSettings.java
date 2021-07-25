package fr.alexpado.bots.cmb.modules.crossout.models.settings;

import fr.alexpado.bots.cmb.modules.discord.models.DiscordUser;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@Entity
@ToString
public class UserSettings {

    @Id
    @Column(length = 20)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    @MapsId
    private DiscordUser user;

    @Column(length = 3)
    private String language = "en";

    private boolean watcherPaused = false;

    public static UserSettings getInstance(DiscordUser user) {

        UserSettings settings = new UserSettings();
        settings.setId(user.getId());
        settings.setUser(user);
        return settings;
    }

    public boolean isWatcherPaused() {

        return this.watcherPaused;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) { return true; }
        if (!(o instanceof UserSettings)) { return false; }
        UserSettings settings = (UserSettings) o;
        return this.id.equals(settings.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.id);
    }

}
