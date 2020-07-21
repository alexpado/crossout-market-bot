package fr.alexpado.bots.cmb.cleaning.entities.discord;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.*;
import fr.alexpado.bots.cmb.cleaning.interfaces.discord.IGuildEntity;
import fr.alexpado.bots.cmb.cleaning.interfaces.discord.IUserEntity;
import net.dv8tion.jda.api.entities.Guild;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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
    private Long       id;
    private String     name;
    private String     imageUrl;
    private String     language;
    @OneToOne
    private UserEntity owner;

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
     * @param owner
     *         The {@link UserEntity} to which this instance belong.
     */
    public GuildEntity(Guild guild, UserEntity owner) {

        this.id       = guild.getIdLong();
        this.name     = guild.getName();
        this.imageUrl = guild.getIconUrl();
        this.owner    = owner;
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
     * Retrieve this {@link Ownable}'s owner.
     *
     * @return The owner.
     */
    @Override
    public IUserEntity getOwner() {

        return this.owner;
    }

    /**
     * Define this {@link Ownable}'s owner.
     *
     * @param owner
     *         The owner.
     */
    @Override
    public void setOwner(IUserEntity owner) {

        if (!(owner instanceof UserEntity)) {
            throw new IllegalArgumentException("The owner of a guild should be an instance of UserEntity.");
        }
        this.owner = (UserEntity) owner;
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
