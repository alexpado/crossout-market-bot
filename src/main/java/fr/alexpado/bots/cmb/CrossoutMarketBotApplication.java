package fr.alexpado.bots.cmb;

import fr.alexpado.bots.cmb.bot.DiscordBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.security.auth.login.LoginException;

@SpringBootApplication
@EnableScheduling
public class CrossoutMarketBotApplication {

    public static AppConfig config;

    public CrossoutMarketBotApplication(AppConfig configurationProvider) {
        config = configurationProvider;
        DiscordBot bot = configurationProvider.discordBot();

        try {
            bot.login();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SpringApplication.run(CrossoutMarketBotApplication.class, args);
    }

}
