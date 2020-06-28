package fr.alexpado.bots.cmb.modules.discord;

import fr.alexpado.bots.cmb.modules.discord.models.DiscordUser;
import fr.alexpado.bots.cmb.modules.discord.models.Session;
import fr.alexpado.bots.cmb.modules.discord.repositories.DiscordUserRepository;
import fr.alexpado.bots.cmb.modules.discord.repositories.SessionRepository;
import fr.alexpado.bots.cmb.modules.discord.utils.DiscordUserQuery;
import fr.alexpado.bots.cmb.modules.discord.utils.SessionQuery;
import fr.alexpado.bots.cmb.modules.discord.web.TokenResponse;
import fr.alexpado.bots.cmb.modules.discord.web.UserResponse;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.function.Consumer;

public class HTTPRequest {

    private final DiscordSettings manager;

    public HTTPRequest(DiscordSettings manager) {

        this.manager = manager;
    }

    private void readOutput(HttpsURLConnection connection, Consumer<JSONObject> onSuccess, Consumer<JSONObject> onError) throws IOException {

        IOException    throwable = null;
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        } catch (IOException e) {
            in        = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            throwable = e;
        }
        String        inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        if (throwable != null) {
            onError.accept(new JSONObject(response.toString()));
        }

        onSuccess.accept(new JSONObject(response.toString()));
    }

    public void doFullAuth(HttpServletRequest request, DiscordUserRepository dur, SessionRepository sr, Consumer<Session> onSuccess, Consumer<JSONObject> onError) {

        String code = request.getHeader("Authorization").replace("Bearer", "").trim();


        this.getUserAuthToken(code, tokenResponse -> this.getUserInfo(tokenResponse.getAccessToken(), userResponse -> {
            DiscordUser user = new DiscordUserQuery(dur).createUpdateUser(userResponse);
            onSuccess.accept(new SessionQuery(sr).createSession(user, code, request.getRemoteAddr()));
        }, onError), onError);
    }

    public void getUserAuthToken(String code, Consumer<TokenResponse> onSuccess, Consumer<JSONObject> onError) {

        try {
            URL                targetUrl  = new URL("https://discordapp.com/api/oauth2/token");
            HttpsURLConnection connection = (HttpsURLConnection) targetUrl.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("User-Agent", this.manager.getUserAgent());
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            connection.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
            String postBuilder = "client_id=" + this.manager.getClientId() + "&client_secret=" + this.manager.getClientSecret() + "&grant_type=" + this.manager
                    .getGrantType() + "&code=" + code + "&redirect_uri=" + this.manager.getRedirectUri() + "&scope=" + this.manager
                                         .getScope();
            wr.writeBytes(postBuilder);
            wr.flush();
            wr.close();

            this.readOutput(connection, jsonObject -> onSuccess.accept(TokenResponse.fromJson(jsonObject)), onError);
        } catch (IOException e) {
            onError.accept(new JSONObject().put("message", e.getMessage()));
        }
    }

    public void getUserInfo(String token, Consumer<UserResponse> onSuccess, Consumer<JSONObject> onError) {

        try {
            URL                targetUrl  = new URL("https://discordapp.com/api/users/@me");
            HttpsURLConnection connection = (HttpsURLConnection) targetUrl.openConnection();

            connection.setRequestProperty("User-Agent", this.manager.getUserAgent());
            connection.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            connection.setRequestProperty("Authorization", "Bearer " + token);

            this.readOutput(connection, jsonObject -> onSuccess.accept(UserResponse.fromJson(jsonObject)), onError);
        } catch (Exception e) {
            onError.accept(new JSONObject().put("message", e.getMessage()));
        }
    }

}
