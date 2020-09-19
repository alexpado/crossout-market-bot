package fr.alexpado.bots.cmb.interfaces.configuration;

/**
 * Interface representing an object that can be used to configure the Market Bot.
 *
 * @author alexpado
 */
public interface CrossoutBotContext extends DiscordBotContext {

    /**
     * Get the URL used to generate graphs.
     *
     * @return A graph URL.
     */
    String getGraphUrl();

    /**
     * Get the default interval for each graph.
     *
     * @return Time in seconds.
     */
    Integer getGraphDuration();

    /**
     * Get the root URL from which the CrossoutDB's API can be accessed.
     *
     * @return Root URL to CrossoutDB's API.
     */
    String getCrossoutDbRootUrl();

}
