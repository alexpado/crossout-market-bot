package fr.alexpado.bots.cmb.cleaning.interfaces.api;

import java.util.List;

/**
 * Interface representing an endpoint providing multiple results.
 *
 * @param <T>
 *         Type of the results.
 */
public interface IterableEndpoint<T> extends Endpoint {

    /**
     * Retrieve all results from the request.
     *
     * @return A list of results.
     */
    List<T> findAll();

}
