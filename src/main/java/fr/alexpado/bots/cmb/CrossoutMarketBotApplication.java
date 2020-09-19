package fr.alexpado.bots.cmb;

import fr.alexpado.bots.cmb.configurations.interfaces.IDiscordConfiguration;
import fr.alexpado.jda.DiscordBotImpl;
import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CrossoutMarketBotApplication extends DiscordBotImpl {

    public static final  String INVITE = "https://discordapp.com/api/oauth2/authorize?client_id=500032551977746453&permissions=59456&scope=bot";
    private static final Logger LOGGER = LoggerFactory.getLogger(CrossoutMarketBotApplication.class);

    /**
     * Create a new instance of {@link DiscordBotImpl} using {@link GatewayIntent#DEFAULT} intents and the provided
     * prefix for the {@link ICommandHandler}.
     */
    public CrossoutMarketBotApplication(IDiscordConfiguration configuration) {

        super(configuration.getPrefix());

        if (configuration.isEnabled()) {
            this.login(configuration.getToken());
        } else {
            LOGGER.warn("The bot is disabled ! Please change this in your `discord.properties` configuration file.");
        }
    }

    public static void main(String[] args) {

        SpringApplication.run(CrossoutMarketBotApplication.class, args);
    }

}
