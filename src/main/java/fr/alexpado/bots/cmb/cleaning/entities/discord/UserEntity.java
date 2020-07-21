package fr.alexpado.bots.cmb.cleaning.entities.discord;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Imageable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.LanguageHolder;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Nameable;
import fr.alexpado.bots.cmb.cleaning.interfaces.crossout.IWatcher;
import fr.alexpado.bots.cmb.cleaning.interfaces.discord.IUserEntity;
import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity class implementing the {@link IUserEntity} interface.
 * <p>
 * This class is simply a wrapper for the {@link User} interface, allowing it to hold some settings and to be save in a
 * database.
 *
 * @author alexpado
 */
@Entity
public class UserEntity implements IUserEntity {

    @Id
    private Long    id;
    private String  name;
    private String  discriminator;
    private String  imageUrl;
    private boolean watcherPaused;
    private String  language;

    /**
     * Create a new {@link UserEntity} with no data. This should not be used, and is present only for the sake of
     * hibernate.
     */
    public UserEntity() {}

    /**
     * Create a new {@link UserEntity} with Discord API's JSON response. This should be used only if the {@link
     * IUserEntity} isn't present in the database, or it will override this {@link IUserEntity}'s settings upon saving.
     *
     * @param source
     *         The {@link JSONObject} to use to initialize this instance.
     */
    public UserEntity(JSONObject source) {

        this.id            = source.getLong("id");
        this.name          = source.getString("username");
        this.discriminator = source.getString("discriminator");
        this.imageUrl      = source.getString("avatar");
        this.watcherPaused = false;
        this.language      = "en";
    }

    /**
     * Create a new {@link UserEntity} with predefined data.
     *
     * @param user
     *         The {@link User} to use to initialize this instance.
     */
    public UserEntity(User user) {

        this.id            = user.getIdLong();
        this.name          = user.getName();
        this.discriminator = user.getDiscriminator();
        this.imageUrl      = user.getAvatarId();
        this.watcherPaused = false;
        this.language      = "en";
    }

    /**
     * Retrieve this {@link IUserEntity}'s discriminator.
     *
     * @return A String.
     */
    @Override
    public String getDiscriminator() {

        return this.discriminator;
    }

    /**
     * Define this {@link IUserEntity}'s discriminator.
     *
     * @param discriminator
     *         The {@link IUserEntity}'s discriminator
     */
    @Override
    public void setDiscriminator(String discriminator) {

        this.discriminator = discriminator;
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
     * Check if no {@link IWatcher}s should be sent to this {@link IUserEntity}.
     *
     * @return True if no {@link IWatcher} should be sent, false instead.
     */
    @Override
    public boolean isWatchersPaused() {

        return this.watcherPaused;
    }

    /**
     * Define if no {@link IWatcher} should be sent to this {@link IUserEntity}.
     *
     * @param watchersPaused
     *         True if no {@link IWatcher} should be sent, false instead.
     */
    @Override
    public void setWatchersPaused(boolean watchersPaused) {

        this.watcherPaused = watchersPaused;
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
