package fr.alexpado.bots.cmb.cleaning.rest.interfaces;

import fr.alexpado.bots.cmb.cleaning.rest.exceptions.RequestException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Interface representing an outgoing request to a REST API.
 *
 * @param <T>
 *         Type of the result entity.
 *
 * @author alexpado
 */
public interface IRestRequest<T> {

    /**
     * Execute the request in an async way.
     */
    void queue();

    /**
     * Execute the request in an async way.
     *
     * @param onSuccess
     *         The consumer that will be used when the request's response is ready.
     */
    void queue(Consumer<T> onSuccess);

    /**
     * Execute the request in an async way.
     *
     * @param onSuccess
     *         The consumer that will be used when the request's response is ready.
     * @param onError
     *         The consumer that will be used if the request encounter an exception.
     */
    void queue(Consumer<T> onSuccess, Consumer<Throwable> onError);

    /**
     * Execute the request in a synchronous way.
     *
     * @return The request's response.
     *
     * @throws IOException
     *         Threw if the request fails.
     * @throws RequestException
     *         Threw if the request's HTTP code isn't 2xx
     */
    T complete() throws IOException;

    /**
     * Retrieve the connection that will be used to send the request.
     *
     * @return An {@link URLConnection} implementation instance.
     */
    URLConnection getConnection() throws IOException;

    /**
     * Retrieve the string version that will be used to send the request.
     *
     * @return An URL as String
     */
    String getTargetUrl();

    /**
     * Retrieve the URL that will be used to send the request.
     *
     * @return An {@link URL} instance.
     *
     * @throws MalformedURLException
     *         Threw if the URL isn't valid.
     */
    URL getUrl() throws MalformedURLException;


    Map<String, String> getQueryParams();

    /**
     * Convert the response's body to the desired type.
     *
     * @param output
     *         The response's body.
     *
     * @return An object of desired type.
     */
    T convert(String output);

    /**
     * This method will be called before sending the request to the REST API. This method can be used to change
     * request's properties, like headers.
     *
     * @param connection
     *         The connection that will be used to do the request.
     */
    void beforeSend(URLConnection connection) throws IOException;

    /**
     * Retrieve the data to use as request body when sending a request.
     *
     * @return The data to use as request body.
     */
    JSONObject getData();
}
