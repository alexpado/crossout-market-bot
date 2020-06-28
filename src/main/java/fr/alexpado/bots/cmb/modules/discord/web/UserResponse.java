package fr.alexpado.bots.cmb.modules.discord.web;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class UserResponse {

    private Long   id;
    private String username;
    private String discriminator;
    private String avatar;

    public static UserResponse fromJson(JSONObject object) {

        UserResponse userResponse = new UserResponse();

        userResponse.id            = object.getLong("id");
        userResponse.username      = object.getString("username");
        userResponse.discriminator = object.getString("discriminator");
        userResponse.avatar        = object.getString("avatar");

        return userResponse;
    }

}
