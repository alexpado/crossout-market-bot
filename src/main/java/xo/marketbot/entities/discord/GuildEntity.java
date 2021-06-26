package xo.marketbot.entities.discord;

import net.dv8tion.jda.api.entities.Guild;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class implementing the {@link GuildEntity} interface.
 * <p>
 * This class is simply a wrapper for the {@link Guild} interface, allowing it to hold some settings and to be save in a database.
 *
 * @author alexpado
 */
@Table(name = "guild")
@Entity
public class GuildEntity {

    @Id
    private Long   id;
    private String name;
    private String icon;
    private String language;

    /**
     * Create a new {@link GuildEntity} with no data. This should not be used, and is present only for the sake of hibernate.
     */
    public GuildEntity() {}

    /**
     * Create a new {@link GuildEntity} with predefined data.
     *
     * @param guild
     *         The {@link Guild} to use to initialize this instance.
     */
    public GuildEntity(Guild guild) {

        this.id       = guild.getIdLong();
        this.name     = guild.getName();
        this.icon     = guild.getIconUrl();
        this.language = "en";
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getIcon() {

        return icon;
    }

    public void setIcon(String icon) {

        this.icon = icon;
    }

    public String getLanguage() {

        return language;
    }

    public void setLanguage(String language) {

        this.language = language;
    }
}
