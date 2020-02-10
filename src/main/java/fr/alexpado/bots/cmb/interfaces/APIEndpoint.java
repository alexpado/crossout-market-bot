package fr.alexpado.bots.cmb.interfaces;

import java.util.List;
import java.util.Optional;

public abstract class APIEndpoint<T, U> {

    private String host;

    /**
     * APIEndpoint constructor.
     *
     * @param host
     *         API Host (root) to use for this endpoint.
     */
    public APIEndpoint(String host) {
        this.host = host;
    }

    /**
     * Get the API Host (root) to use for this endpoint.
     *
     * @return API Host (root) to use for this endpoint.
     */
    public String getHost() {
        return host;
    }

    /**
     * Retrieve one result using the unique identifier provided.
     *
     * @param identifier
     *         Unique value identifying the requested result.
     *
     * @return An optional result.
     */
    public abstract Optional<T> getOne(U identifier);

    /**
     * Retrieve all results from this endpoint.
     *
     * @return A list of results.
     */
    public abstract List<T> getAll();

}
