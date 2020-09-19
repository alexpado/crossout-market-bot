package xo.marketbot.web.auth.entities.requests;

import fr.alexpado.lib.rest.RestAction;
import fr.alexpado.lib.rest.enums.RequestMethod;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import xo.marketbot.configurations.interfaces.IDiscordConfiguration;
import xo.marketbot.web.auth.entities.responses.TokenResponse;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class TokenRequest extends RestAction<TokenResponse> {

    private final IDiscordConfiguration configuration;
    private final String                code;

    public TokenRequest(IDiscordConfiguration context, String code) {

        this.configuration = context;
        this.code          = code;
    }

    @Override
    public @NotNull RequestMethod getRequestMethod() {

        return RequestMethod.POST;
    }

    @Override
    public @NotNull String getRequestURL() {

        return "https://discordapp.com/api/oauth2/token";
    }

    @Override
    public @NotNull Map<String, String> getRequestHeaders() {

        Map<String, String> headers = new HashMap<>();

        headers.put("User-Agent", "XOMarketBot");
        headers.put("Accept-Language", "en-US,en;q=0.5");
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        return headers;
    }

    @Override
    public @NotNull String getRequestBody() {

        Map<String, String> formData = new HashMap<>();

        formData.put("client_id", this.configuration.getClientId());
        formData.put("client_secret", this.configuration.getClientSecret());
        formData.put("grant_type", this.configuration.getGrantType());
        formData.put("code", this.code);
        formData.put("redirect_uri", this.configuration.getRedirectUri());
        formData.put("scope", this.configuration.getScope());

        return RestAction.mergeMapAsQuery(formData);
    }

    @Override
    public TokenResponse convert(byte[] requestBody) {

        return new TokenResponse(new JSONObject(new String(requestBody, StandardCharsets.UTF_8)));
    }

}
