package xo.marketbot.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import xo.marketbot.configurations.interfaces.IDiscordConfiguration;

@Configuration
public class DiscordConfiguration implements IDiscordConfiguration {

    @Value("${discord.bot.token}")
    private String token;

    @Value("${discord.bot.enabled:true}")
    private boolean enabled;

    @Override
    public String getToken() {

        return this.token;
    }

    @Override
    public boolean isEnabled() {

        return this.enabled;
    }

}
