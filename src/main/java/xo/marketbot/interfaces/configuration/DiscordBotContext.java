package xo.marketbot.interfaces.configuration;

/**
 * Interface representing an object that can be used to configure a Discord Bot.
 *
 * @author alexpado
 */
public interface DiscordBotContext {

    /**
     * Get the bot prefix that will be used for commands.
     *
     * @return A prefix.
     */
    String getBotPrefix();

    /**
     * Get the bot token that will be used for login in with JDA.
     *
     * @return A Token
     */
    String getBotToken();

}
