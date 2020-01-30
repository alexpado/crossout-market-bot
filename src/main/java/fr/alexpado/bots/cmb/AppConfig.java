package fr.alexpado.bots.cmb;

import fr.alexpado.bots.cmb.crossout.repositories.TranslationRepository;
import fr.alexpado.bots.cmb.crossout.repositories.WatcherRepository;
import fr.alexpado.bots.cmb.discord.DiscordBot;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class AppConfig {

    @Autowired
    public WatcherRepository watcherRepository;

    @Autowired
    public TranslationRepository translationRepository;

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

    @Bean
    public AppConfig configurationProvider() {
        return new AppConfig();
    }

    public DiscordBot discordBot() {
        return new DiscordBot(this);
    }

}
