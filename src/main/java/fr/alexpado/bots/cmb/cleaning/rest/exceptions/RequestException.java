package fr.alexpado.bots.cmb.cleaning.rest.exceptions;

import fr.alexpado.bots.cmb.cleaning.rest.interfaces.IRestRequest;

/**
 * Exception used when an {@link IRestRequest} fails because the remote server didn't returned a 2xx HTTP code.
 *
 * @author alexpado
 */
public class RequestException extends RuntimeException {

    private final String response;

    /**
     * Create a new exception.
     *
     * @param response
     *         The response sent by the REST API. Usually contains more information about the error.
     * @param message
     *         The message defined by the implementation of {@link IRestRequest}.
     */
    public RequestException(String response, String message) {

        super(message);
        this.response = response;
    }

    /**
     * Retrieve the response sent by the REST API. Usually contains more information about the error.
     *
     * @return A String, probably formatted as JSON.
     */
    public String getResponse() {

        return response;
    }
}
