package fr.alexpado.bots.cmb.modules.discord.models;

import fr.alexpado.bots.cmb.modules.discord.web.UserResponse;
import lombok.Getter;
import lombok.ToString;
import net.dv8tion.jda.api.entities.User;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Getter
@ToString
public class DiscordUser implements Serializable {

    @Id
    private Long id;

    private String avatar;

    private String discriminator;

    private String name;

    public static DiscordUser getInstance(UserResponse response) {
        return new DiscordUser().refresh(response);
    }

    public static DiscordUser getInstance(User user) {
        return new DiscordUser().refresh(user);
    }

    public DiscordUser refresh(UserResponse response) {
        this.id = response.getId();
        this.avatar = response.getAvatar();
        this.discriminator = response.getDiscriminator();
        this.name = response.getUsername();

        return this;
    }

    public DiscordUser refresh(User user) {
        this.id = user.getIdLong();
        this.avatar = user.getAvatarId();
        this.discriminator = user.getDiscriminator();
        this.name = user.getName();

        return this;
    }

}
