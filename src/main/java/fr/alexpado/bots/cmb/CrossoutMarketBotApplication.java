package fr.alexpado.bots.cmb;

import fr.alexpado.bots.cmb.discord.DiscordBot;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.security.auth.login.LoginException;

@SpringBootApplication
public class CrossoutMarketBotApplication {

    public static void main(String[] args) {
        SpringApplication.run(CrossoutMarketBotApplication.class, args);
    }

    public CrossoutMarketBotApplication(AppConfig configurationProvider) {
        DiscordBot bot = configurationProvider.discordBot();

        try {
            bot.login();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

}
