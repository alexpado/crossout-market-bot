package xo.marketbot.entities.discord;

import net.dv8tion.jda.api.entities.User;
import org.json.JSONObject;
import xo.marketbot.web.auth.entities.responses.UserResponse;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Entity class implementing the {@link IUserEntity} interface.
 * <p>
 * This class is simply a wrapper for the {@link net.dv8tion.jda.api.entities.User} interface, allowing it to hold some
 * settings and to be save in a database.
 *
 * @author alexpado
 */
@Table(name = "user")
@Entity
public class UserEntity {

    @Id
    private Long    id;
    private String  username;
    private String  discriminator;
    private String  avatar;
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
        this.username      = source.getString("username");
        this.discriminator = source.getString("discriminator");
        this.avatar        = source.getString("avatar");
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
        this.username      = user.getName();
        this.discriminator = user.getDiscriminator();
        this.avatar        = user.getAvatarId();
        this.watcherPaused = false;
        this.language      = "en";
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getDiscriminator() {

        return discriminator;
    }

    public void setDiscriminator(String discriminator) {

        this.discriminator = discriminator;
    }

    public String getAvatar() {

        return avatar;
    }

    public void setAvatar(String avatar) {

        this.avatar = avatar;
    }

    public boolean isWatcherPaused() {

        return watcherPaused;
    }

    public void setWatcherPaused(boolean watcherPaused) {

        this.watcherPaused = watcherPaused;
    }

    public String getLanguage() {

        return language;
    }

    public void setLanguage(String language) {

        this.language = language;
    }

    public void merge(UserResponse userResponse) {

        this.setUsername(userResponse.getUsername());
        this.setDiscriminator(userResponse.getDiscriminator());
        this.setAvatar(userResponse.getAvatar());
    }


}
