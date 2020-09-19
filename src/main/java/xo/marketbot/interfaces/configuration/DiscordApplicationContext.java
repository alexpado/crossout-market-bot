package xo.marketbot.interfaces.configuration;

/**
 * Interface representing an object that can be used to configure a Discord Application.
 *
 * @author alexpado
 */
public interface DiscordApplicationContext {

    /**
     * Get the Discord Application Client ID.
     *
     * @return Client ID
     */
    String getApplicationClientId();

    /**
     * Get the Discord Application Client Secret.
     *
     * @return Client Secret
     */
    String getApplicationClientSecret();

    /**
     * Get the Discord Application redirect URI.
     *
     * @return Redirect URI.
     */
    String getApplicationRedirectUri();

}
