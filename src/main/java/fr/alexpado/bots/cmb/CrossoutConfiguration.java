package fr.alexpado.bots.cmb;

import fr.alexpado.bots.cmb.bot.DiscordBot;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Getter
@Component
public class CrossoutConfiguration {

    private final RepositoryAccessor repositoryAccessor;

    @Value("${http.identity.group}")
    private String identityGroup;

    @Value("${http.identity.name}")
    private String identityName;

    @Value("${api.host}")
    private String apiHost;

    @Value("${discord.token}")
    private String discordToken;

    @Value("${bot.prefix}")
    private String discordPrefix;

    @Value("${watchers.timeout}")
    private long watchersTimeout;

    @Value("${i18n.default}")
    private String defaultLocale;

    @Value("${bot.item.graph.duration}")
    private Long graphInterval;

    @Value("${bot.item.graph.source}")
    private String chartUrl;

    @Value("${discord.clientId}")
    private String clientId;

    @Value("${discord.clientSecret}")
    private String clientSecret;

    @Value("${discord.grantType:authorization_code}")
    private String grantType;

    @Value("${discord.redirectUri}")
    private String redirectUri;

    @Value("${discord.scope:identify}")
    private String scope;

    @Value("${chart.cache:true}")
    private boolean cacheEnabled;

    public CrossoutConfiguration(RepositoryAccessor repositoryAccessor) {

        this.repositoryAccessor = repositoryAccessor;
    }

    public DiscordBot discordBot() {

        return new DiscordBot(this);
    }

}
