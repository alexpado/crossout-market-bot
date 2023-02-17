package xo.marketbot.entities.discord;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.Objects;

/**
 * Entity class implementing the {@link UserEntity} interface.
 * <p>
 * This class is simply a wrapper for the {@link User} interface, allowing it to hold some settings and to be saved in a
 * database.
 */
@Table(name = "user")
@Entity
public class UserEntity {

    @Id
    private long     id;
    private String   username;
    private String   discriminator;
    private String   avatar;
    private boolean  watcherPaused;
    @ManyToOne
    private Language language;

    /**
     * Create a new {@link UserEntity} with no data. This should not be used, and is present only for the sake of
     * hibernate.
     */
    public UserEntity() {}

    /**
     * Create a new {@link UserEntity} with Discord API JSON response. This should be used only if the
     * {@link UserEntity} isn't present in the database, or it will override this {@link UserEntity}'s settings upon
     * saving.
     *
     * @param source
     *         The {@link JSONObject} to use to initialize this instance.
     * @param language
     *         The default {@link Language} to use.
     */
    public UserEntity(JSONObject source, Language language) {

        this.id            = source.getLong("id");
        this.username      = source.getString("username");
        this.discriminator = source.getString("discriminator");
        this.avatar        = source.getString("avatar");
        this.watcherPaused = false;
        this.language      = language;
    }

    /**
     * Create a new {@link UserEntity} with predefined data.
     *
     * @param user
     *         The {@link User} to use to initialize this instance.
     * @param language
     *         The default {@link Language} to use.
     */
    public UserEntity(User user, Language language) {

        this.id            = user.getIdLong();
        this.username      = user.getName();
        this.discriminator = user.getDiscriminator();
        this.avatar        = user.getAvatarId();
        this.watcherPaused = false;
        this.language      = language;
    }

    /**
     * Retrieve this {@link UserEntity}'s ID.
     *
     * @return The ID
     */
    public Long getId() {

        return id;
    }

    /**
     * Retrieve this {@link UserEntity}'s username.
     *
     * @return The username
     */
    public String getUsername() {

        return username;
    }

    /**
     * Define this {@link UserEntity}'s username.
     *
     * @param username
     *         The username
     */
    public void setUsername(String username) {

        this.username = username;
    }

    /**
     * Retrieve this {@link UserEntity}'s discriminator.
     *
     * @return The discriminator
     */
    public String getDiscriminator() {

        return discriminator;
    }

    /**
     * Define this {@link UserEntity}'s discriminator.
     *
     * @param discriminator
     *         The discriminator
     */
    public void setDiscriminator(String discriminator) {

        this.discriminator = discriminator;
    }

    /**
     * Retrieve this {@link UserEntity}'s avatar url.
     *
     * @return The avatar url.
     */
    public String getAvatar() {

        return avatar;
    }

    /**
     * Define this {@link UserEntity}'s avatar url.
     *
     * @param avatar
     *         The avatar url.
     */
    public void setAvatar(String avatar) {

        this.avatar = avatar;
    }

    /**
     * Check if the {@link Watcher} of this {@link UserEntity} should be executed.
     *
     * @return True if the {@link Watcher} shouldn't be executed, false otherwise.
     */
    public boolean isWatcherPaused() {

        return watcherPaused;
    }

    /**
     * Define if the {@link Watcher} of this {@link UserEntity} should be executed.
     *
     * @param watcherPaused
     *         True if the {@link Watcher} shouldn't be executed, false otherwise.
     */
    public void setWatcherPaused(boolean watcherPaused) {

        this.watcherPaused = watcherPaused;
    }

    /**
     * Retrieve this {@link UserEntity}'s language.
     *
     * @return The language.
     */
    @Nullable
    public Language getLanguage() {

        return this.language;
    }

    /**
     * Define this {@link UserEntity}'s language.
     *
     * @param language
     *         The language.
     */
    public void setLanguage(@Nullable Language language) {

        this.language = language;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof UserEntity user) {
            return this.getId().equals(user.getId());
        } else if (o instanceof User user) {
            return this.getId().equals(user.getIdLong());
        }
        return false;
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.getId());
    }

}
