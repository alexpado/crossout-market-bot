package fr.alexpado.bots.cmb.cleaning.rest.method;

import fr.alexpado.bots.cmb.cleaning.rest.impl.OutgoingRestRequest;

/**
 * Class representing a POST HTTP request to a REST API.
 *
 * @param <T>
 *         The type of the entity returned by the REST API.
 *
 * @author alexpado
 */
public abstract class PostRestRequest<T> extends OutgoingRestRequest<T> {

    /**
     * Retrieve the method to execute this request.
     *
     * @return One of "POST", "PUT", "PATCH" or "DELETE"
     */
    @Override
    protected final String getMethod() {

        return "POST";
    }

}
