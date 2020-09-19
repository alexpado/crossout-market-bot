package xo.marketbot.auth.entities.requests;

import fr.alexpado.lib.rest.RestAction;
import fr.alexpado.lib.rest.enums.RequestMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import xo.marketbot.auth.entities.responses.UserResponse;
import xo.marketbot.configurations.interfaces.IDiscordConfiguration;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class UserRequest extends RestAction<UserResponse> {

    private final String                token;
    private final IDiscordConfiguration configuration;

    public UserRequest(IDiscordConfiguration configuration, String token) {

        this.configuration = configuration;
        this.token         = token;
    }

    @Override
    public @NotNull RequestMethod getRequestMethod() {

        return RequestMethod.GET;
    }

    @Override
    public @NotNull String getRequestURL() {

        return "https://discordapp.com/api/users/@me";
    }

    @Override
    public @NotNull Map<String, String> getRequestHeaders() {

        Map<String, String> headers = new HashMap<>();

        headers.put("User-Agent", "XO-Market-Bot/v3");
        headers.put("Accept-Language", "en-US,en;q=0.5");
        headers.put("Authorization", String.format("Bearer %s", this.token));

        return headers;
    }

    @Override
    public UserResponse convert(byte[] requestBody) {

        return new UserResponse(new JSONObject(new String(requestBody, StandardCharsets.UTF_8)));
    }
}
