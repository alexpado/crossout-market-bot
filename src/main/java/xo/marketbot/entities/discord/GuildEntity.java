package xo.marketbot.entities.discord;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Entity class implementing the {@link GuildEntity} interface.
 * <p>
 * This class is simply a wrapper for the {@link Guild} interface, allowing it to hold some settings and to be saved in
 * a database.
 */
@Table(name = "guild")
@Entity
public class GuildEntity {

    @Id
    private Long     id;
    private String   name;
    private String   icon;
    @ManyToOne
    private Language language;
    private boolean  active = false;

    /**
     * Create a new {@link GuildEntity} with no data. This should not be used, and is present only for the sake of
     * hibernate.
     */
    public GuildEntity() {}

    /**
     * Create a new {@link GuildEntity} with predefined data.
     *
     * @param guild
     *         The {@link Guild} to use to initialize this instance.
     * @param language
     *         The default {@link Language} to use.
     */
    public GuildEntity(Guild guild, Language language) {

        this.id       = guild.getIdLong();
        this.name     = guild.getName();
        this.icon     = guild.getIconUrl();
        this.language = language;
        this.active   = true;
    }

    /**
     * Retrieve this {@link GuildEntity}'s ID.
     *
     * @return The ID
     */
    public Long getId() {

        return this.id;
    }

    /**
     * Retrieve this {@link GuildEntity}'s name.
     *
     * @return The name.
     */
    public String getName() {

        return this.name;
    }

    /**
     * Define this {@link GuildEntity}'s name.
     *
     * @param name
     *         The name.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Retrieve this {@link GuildEntity}'s icon.
     *
     * @return The icon url.
     */
    public String getIcon() {

        return this.icon;
    }

    /**
     * Define this {@link GuildEntity}'s icon.
     *
     * @param icon
     *         The icon url.
     */
    public void setIcon(String icon) {

        this.icon = icon;
    }

    /**
     * Retrieve this {@link GuildEntity}'s language.
     *
     * @return The language.
     */
    @Nullable
    public Language getLanguage() {

        return this.language;
    }

    /**
     * Define this {@link GuildEntity}'s language.
     *
     * @param language
     *         The language.
     */
    public void setLanguage(@Nullable Language language) {

        this.language = language;
    }

    /**
     * Check if this {@link GuildEntity} is active. An active {@link GuildEntity} means that the associated
     * {@link Guild} is still accessible through any {@link JDA} instance.
     *
     * @return True if active, false otherwise.
     */
    public boolean isActive() {

        return active;
    }

    /**
     * Define if this {@link GuildEntity} is active.
     *
     * @param active
     *         True if active, false otherwise.
     */
    public void setActive(boolean active) {

        this.active = active;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof GuildEntity guild) {
            return this.getId().equals(guild.getId());
        } else if (o instanceof Guild guild) {
            return this.getId().equals(guild.getIdLong());
        }
        return false;
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.getId());
    }

}
