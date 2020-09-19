package fr.alexpado.bots.cmb.auth.entities.responses;


import fr.alexpado.bots.cmb.entities.discord.UserEntity;
import org.json.JSONObject;

public class UserResponse {

    private final Long   id;
    private final String username;
    private final String discriminator;
    private final String avatar;

    public UserResponse(JSONObject source) {

        this.id            = source.getLong("id");
        this.username      = source.getString("username");
        this.discriminator = source.getString("discriminator");
        this.avatar        = source.getString("avatar");
    }

    public Long getId() {

        return this.id;
    }

    public String getUsername() {

        return this.username;
    }

    public String getDiscriminator() {

        return this.discriminator;
    }

    public String getAvatar() {

        return this.avatar;
    }

    public UserEntity toUser() {

        UserEntity user = new UserEntity();

        user.setId(this.id);
        user.setName(this.username);
        user.setDiscriminator(this.discriminator);
        user.setImageUrl(this.avatar);

        return user;
    }
}
