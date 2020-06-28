package fr.alexpado.bots.cmb.modules.discord;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class DiscordSettings {

    private final String userAgent;

    private final String clientId;

    private final String clientSecret;

    private final String grantType;

    private final String redirectUri;

    private final String scope;

    public DiscordSettings(CrossoutConfiguration configuration) {

        this.userAgent    = configuration.getIdentityName();
        this.clientId     = configuration.getClientId();
        this.clientSecret = configuration.getClientSecret();
        this.grantType    = configuration.getGrantType();
        this.redirectUri  = configuration.getRedirectUri();
        this.scope        = configuration.getScope();
    }

}
