package fr.alexpado.bots.cmb.auth;


import fr.alexpado.bots.cmb.auth.entities.requests.TokenRequest;
import fr.alexpado.bots.cmb.auth.entities.requests.UserRequest;
import fr.alexpado.bots.cmb.auth.entities.responses.TokenResponse;
import fr.alexpado.bots.cmb.auth.entities.responses.UserResponse;
import fr.alexpado.bots.cmb.configurations.interfaces.IDiscordConfiguration;
import fr.alexpado.lib.rest.RestAction;
import org.springframework.stereotype.Service;

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
