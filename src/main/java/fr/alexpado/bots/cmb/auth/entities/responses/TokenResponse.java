package fr.alexpado.bots.cmb.auth.entities.responses;

import fr.alexpado.bots.cmb.auth.entities.Session;
import org.json.JSONObject;

public class TokenResponse {

    private final String  accessToken;
    private final String  refreshToken;
    private       Integer expiresIn;

    public TokenResponse(JSONObject source) {

        this.accessToken  = source.getString("access_token");
        this.expiresIn    = source.getInt("expires_in");
        this.refreshToken = source.getString("refresh_token");
    }

    public TokenResponse(Session session) {

        this.accessToken  = session.getToken();
        this.refreshToken = session.getRefreshToken();
    }

    public String getAccessToken() {

        return this.accessToken;
    }

    public Integer getExpiresIn() {

        return this.expiresIn;
    }

    public String getRefreshToken() {

        return this.refreshToken;
    }

}
