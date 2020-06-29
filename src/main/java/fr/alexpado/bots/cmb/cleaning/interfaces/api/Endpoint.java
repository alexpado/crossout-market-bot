package fr.alexpado.bots.cmb.cleaning.interfaces.api;

import fr.alexpado.bots.cmb.cleaning.interfaces.configuration.CrossoutBotContext;

/**
 * Interface representing a CrossoutDB API endpoint.
 */
public interface Endpoint {

    /**
     * Retrieve the endpoint to append to {@link CrossoutBotContext#getCrossoutDbRootUrl()}.
     *
     * @return The endpoint.
     */
    String getEndpoint();

    /**
     * Set a parameter that will be used when sending a request to CrossoutDB's API.
     *
     * @param name
     *         Name of the parameter to set
     * @param value
     *         Value of the parameter
     */
    void setRequestParameter(String name, String value);

}
