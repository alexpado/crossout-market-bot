package xo.marketbot.entities.discord;

import net.dv8tion.jda.api.entities.Guild;
import xo.marketbot.entities.interfaces.common.Identifiable;
import xo.marketbot.entities.interfaces.common.Imageable;
import xo.marketbot.entities.interfaces.common.LanguageHolder;
import xo.marketbot.entities.interfaces.common.Nameable;
import xo.marketbot.entities.interfaces.discord.IGuildEntity;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity class implementing the {@link IGuildEntity} interface.
 * <p>
 * This class is simply a wrapper for the {@link Guild} interface, allowing it to hold some settings and to be save in a
 * database.
 *
 * @author alexpado
 */
@Entity
public class GuildEntity implements IGuildEntity {

    @Id
    private Long   id;
    private String name;
    private String imageUrl;
    private String language;

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
     */
    public GuildEntity(Guild guild) {

        this.id       = guild.getIdLong();
        this.name     = guild.getName();
        this.imageUrl = guild.getIconUrl();
    }

    /**
     * Retrieve this {@link Identifiable}'s ID.
     *
     * @return An ID.
     */
    @Override
    public Long getId() {

        return this.id;
    }

    /**
     * Retrieve this {@link Imageable}'s image url.
     *
     * @return An image url
     */
    @Override
    public String getImageUrl() {

        return this.imageUrl;
    }

    /**
     * Define this {@link Imageable}'s image url.
     *
     * @param url
     *         An image url.
     */
    @Override
    public void setImageUrl(String url) {

        this.imageUrl = url;
    }

    /**
     * Retrieve this {@link Nameable}'s name.
     *
     * @return The name.
     */
    @Override
    public String getName() {

        return this.name;
    }

    /**
     * Define this {@link Nameable}'s name.
     *
     * @param name
     *         The name.
     */
    @Override
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Retrieve this {@link LanguageHolder}'s language. The language is usually represented by two characters.
     *
     * @return This {@link LanguageHolder}'s language.
     */
    @Override
    public String getLanguage() {

        return this.language;
    }

    /**
     * Define this {@link LanguageHolder}'s language. The language is usually represented by two characters.
     *
     * @param language
     *         This {@link LanguageHolder}'s language.
     */
    @Override
    public void setLanguage(String language) {

        this.language = language;
    }
}
