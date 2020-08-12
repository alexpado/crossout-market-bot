package fr.alexpado.bots.cmb.cleaning;

import fr.alexpado.bots.cmb.cleaning.interfaces.configuration.DiscordBotContext;
import fr.alexpado.jda.DiscordBotImpl;
import fr.alexpado.jda.services.commands.interfaces.ICommandHandler;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

@Service
public class XoDBBot extends DiscordBotImpl {

    public static final String INVITE = "https://discordapp.com/api/oauth2/authorize?client_id=500032551977746453&permissions=59456&scope=bot";

    private final DiscordBotContext context;

    /**
     * Create a new instance of {@link DiscordBotImpl} using {@link GatewayIntent#DEFAULT} intents and the provided
     * prefix for the {@link ICommandHandler}.
     *
     * @param context
     *         The {@link DiscordBotContext} to use to configure this {@link DiscordBotImpl} instance.
     */
    public XoDBBot(@NotNull DiscordBotContext context) {

        super(context.getBotPrefix());
        this.context = context;
    }

    /**
     * Tell this {@link DiscordBotImpl} to login to Discord using the {@link DiscordBotContext} provided when
     * instantiating this {@link DiscordBotImpl}.
     * <p>
     * You may register commands before doing that.
     */
    public void login() {

        super.login(this.context.getBotToken());
    }
}
