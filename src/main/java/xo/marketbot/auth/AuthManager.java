package xo.marketbot.auth;


import fr.alexpado.lib.rest.RestAction;
import org.springframework.stereotype.Service;
import xo.marketbot.auth.entities.requests.TokenRequest;
import xo.marketbot.auth.entities.requests.UserRequest;
import xo.marketbot.auth.entities.responses.TokenResponse;
import xo.marketbot.auth.entities.responses.UserResponse;
import xo.marketbot.configurations.interfaces.IDiscordConfiguration;

@Service
public class AuthManager {

    private final IDiscordConfiguration context;

    public AuthManager(IDiscordConfiguration context) {

        this.context = context;
    }

    public RestAction<TokenResponse> tryAuthAction(String code) {

        return new TokenRequest(this.context, code);
    }

    public RestAction<UserResponse> validateTokenAction(String token) {

        return new UserRequest(this.context, token);
    }
}
