package fr.alexpado.bots.cmb.cleaning;

import fr.alexpado.bots.cmb.cleaning.interfaces.configuration.CrossoutBotContext;
import fr.alexpado.bots.cmb.cleaning.interfaces.configuration.DiscordApplicationContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class BeanService {

    @Bean
    public DiscordApplicationContext applicationContext(@Value("${discord.clientId}") String clientId, @Value("${discord.clientSecret}") String clientSecret, @Value("${discord.redirectUri}") String redirectUri) {

        return new DiscordApplicationContext() {

            @Override
            public String getApplicationClientId() {

                return clientId;
            }

            @Override
            public String getApplicationClientSecret() {

                return clientSecret;
            }

            @Override
            public String getApplicationRedirectUri() {

                return redirectUri;
            }
        };
    }

    @Bean
    public CrossoutBotContext crossoutBotContext(@Value("${bot.prefix}") String prefix, @Value("${discord.token}") String token, @Value("${bot.item.graph.source}") String graphUrl, @Value("${bot.item.graph.duration}") Integer graphDuration, @Value("${api.host}") String rootUrl) {

        return new CrossoutBotContext() {

            @Override
            public String getGraphUrl() {

                return graphUrl;
            }

            @Override
            public Integer getGraphDuration() {

                return graphDuration;
            }

            @Override
            public String getCrossoutDbRootUrl() {

                return rootUrl;
            }

            @Override
            public String getBotPrefix() {

                return prefix;
            }

            @Override
            public String getBotToken() {

                return token;
            }
        };
    }

}
