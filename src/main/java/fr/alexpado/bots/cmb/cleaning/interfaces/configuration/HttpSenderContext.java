package fr.alexpado.bots.cmb.cleaning.interfaces.configuration;

/**
 * Interface representing an object that can be used to configure an http request.
 *
 * @author alexpado
 */
public interface HttpSenderContext {

    /**
     * Get the group from this application. This is used to regroup multiple applications under the same group.
     *
     * @return The group name.
     */
    String getUserAgentGroup();

    /**
     * Get the name from this application under the user agent group.
     *
     * @return The user agent name.
     */
    String getUserAgentName();

}
