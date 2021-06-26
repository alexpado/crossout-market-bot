package xo.marketbot;

import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import xo.marketbot.configurations.interfaces.IDiscordConfiguration;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.library.DiscordBotImpl;
import xo.marketbot.library.services.commands.interfaces.ICommandEvent;
import xo.marketbot.library.services.commands.interfaces.ICommandHandler;
import xo.marketbot.repositories.ChannelEntityRepository;
import xo.marketbot.repositories.GuildEntityRepository;
import xo.marketbot.repositories.UserEntityRepository;

@SpringBootApplication
@EnableScheduling
public class XoMarketApplication extends DiscordBotImpl {

    public static final  String INVITE = "https://discordapp.com/api/oauth2/authorize?client_id=500032551977746453&permissions=59456&scope=bot";
    private static final Logger LOGGER = LoggerFactory.getLogger(XoMarketApplication.class);

    public static String NOTICE = null;

    /**
     * Create a new instance of {@link DiscordBotImpl} using {@link GatewayIntent#DEFAULT} intents and the provided prefix for the {@link
     * ICommandHandler}.
     */
    public XoMarketApplication(IDiscordConfiguration configuration, GuildEntityRepository guildRepository, ChannelEntityRepository channelRepository, UserEntityRepository userRepository, TranslationProvider provider) {

        super(configuration.getPrefix(), guildRepository, channelRepository, userRepository, provider);

        if (configuration.isEnabled()) {
            this.login(configuration.getToken());
        } else {
            LOGGER.warn("The bot is disabled ! Please change this in your `discord.properties` configuration file.");
        }
    }

    public static void main(String[] args) {

        SpringApplication.run(XoMarketApplication.class, args);
    }

    /**
     * Called by {@link ICommandHandler}
     *
     * @param event
     *         The {@link ICommandEvent} triggering this event.
     */
    @Override
    public void onCommandExecuted(@NotNull ICommandEvent event) {

        if (event.getContext().getEvent().getJDA().getPresence().getStatus() == OnlineStatus.DO_NOT_DISTURB) {
            event.setCancelled(true);

            // TODO: Send XoDB Offline message
        }
    }

}
