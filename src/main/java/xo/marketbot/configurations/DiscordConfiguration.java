package xo.marketbot.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import xo.marketbot.configurations.interfaces.IDiscordConfiguration;

@Configuration
@PropertySources({
        @PropertySource(value = "file:./discord.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:discord.properties", ignoreResourceNotFound = true)
})
public class DiscordConfiguration implements IDiscordConfiguration {

    @Value("${discord.application.client-id")
    private String clientId;

    @Value("${discord.application.client-secret")
    private String clientSecret;

    @Value("${discord.application.grant-type")
    private String grantType;

    @Value("${discord.application.redirect-uri")
    private String redirectUri;

    @Value("${discord.application.scope")
    private String scope;

    @Value("${discord.bot.token}")
    private String token;

    @Value("${discord.bot.prefix")
    private String prefix;

    @Value("${discord.bot.enabled:true}")
    private boolean enabled;

    @Override
    public String getClientId() {

        return this.clientId;
    }

    @Override
    public String getClientSecret() {

        return this.clientSecret;
    }

    @Override
    public String getGrantType() {

        return this.grantType;
    }

    @Override
    public String getRedirectUri() {

        return this.redirectUri;
    }

    @Override
    public String getScope() {

        return this.scope;
    }

    @Override
    public String getToken() {

        return this.token;
    }

    @Override
    public String getPrefix() {

        return this.prefix;
    }

    @Override
    public boolean isEnabled() {

        return this.enabled;
    }
}
