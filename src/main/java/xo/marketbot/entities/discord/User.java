package xo.marketbot.entities.discord;

import org.json.JSONObject;
import xo.marketbot.interfaces.common.Identifiable;
import xo.marketbot.interfaces.common.Imageable;
import xo.marketbot.interfaces.common.LanguageHolder;
import xo.marketbot.interfaces.common.Nameable;
import xo.marketbot.interfaces.crossout.IWatcher;
import xo.marketbot.interfaces.discord.IUserEntity;
import xo.marketbot.web.auth.entities.responses.UserResponse;

import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Entity class implementing the {@link IUserEntity} interface.
 * <p>
 * This class is simply a wrapper for the {@link net.dv8tion.jda.api.entities.User} interface, allowing it to hold some
 * settings and to be save in a database.
 *
 * @author alexpado
 */
@Entity
public class User implements IUserEntity {

    @Id
    private Long    id;
    private String  name;
    private String  discriminator;
    private String  avatar;
    private boolean watcherPaused;
    private String  language;

    /**
     * Create a new {@link User} with no data. This should not be used, and is present only for the sake of
     * hibernate.
     */
    public User() {}

    /**
     * Create a new {@link User} with Discord API's JSON response. This should be used only if the {@link IUserEntity}
     * isn't present in the database, or it will override this {@link IUserEntity}'s settings upon saving.
     *
     * @param source
     *         The {@link JSONObject} to use to initialize this instance.
     */
    public User(JSONObject source) {

        this.id            = source.getLong("id");
        this.name          = source.getString("username");
        this.discriminator = source.getString("discriminator");
        this.avatar        = source.getString("avatar");
        this.watcherPaused = false;
        this.language      = "en";
    }

    /**
     * Create a new {@link User} with predefined data.
     *
     * @param user
     *         The {@link net.dv8tion.jda.api.entities.User} to use to initialize this instance.
     */
    public User(net.dv8tion.jda.api.entities.User user) {

        this.id            = user.getIdLong();
        this.name          = user.getName();
        this.discriminator = user.getDiscriminator();
        this.avatar        = user.getAvatarId();
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

        return this.avatar;
    }

    /**
     * Define this {@link Imageable}'s image url.
     *
     * @param url
     *         An image url.
     */
    @Override
    public void setImageUrl(String url) {

        this.avatar = url;
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

    public void merge(UserResponse userResponse) {

        this.setName(userResponse.getUsername());
        this.setDiscriminator(userResponse.getDiscriminator());
        this.setImageUrl(userResponse.getAvatar());
    }
}
