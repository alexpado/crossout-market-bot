package fr.alexpado.bots.cmb.modules.discord.web;

import lombok.Getter;
import org.json.JSONObject;

@Getter
public class TokenResponse {

    private String  accessToken;
    private String  tokenType;
    private Integer expiresIn;
    private String  refreshToken;
    private String  scope;
    private Long    generationTime;

    public static TokenResponse fromJson(JSONObject object) {

        TokenResponse response = new TokenResponse();

        response.accessToken    = object.getString("access_token");
        response.tokenType      = object.getString("token_type");
        response.expiresIn      = object.getInt("expires_in");
        response.refreshToken   = object.getString("refresh_token");
        response.scope          = object.getString("scope");
        response.generationTime = System.currentTimeMillis() / 1000;

        return response;
    }

}
