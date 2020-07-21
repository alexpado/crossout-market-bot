package fr.alexpado.bots.cmb.cleaning.rest.method;

import fr.alexpado.bots.cmb.cleaning.rest.impl.HttpRestRequest;
import org.json.JSONObject;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Class representing a GET HTTP request to a REST API.
 *
 * @param <T>
 *         The type of the entity returned by the REST API.
 *
 * @author alexpado
 */
public abstract class GetRestRequest<T> extends HttpRestRequest<T> {

    /**
     * This method will be called before sending the request to the REST API. This method can be used to change
     * request's properties, like headers.
     *
     * @param connection
     *         The connection that will be used to do the request.
     */
    @Override
    public void beforeSend(URLConnection connection) throws IOException {

        HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

        httpURLConnection.setRequestMethod("GET");
        httpURLConnection.setRequestProperty("Accept", "application/json");
    }

    /**
     * Retrieve the data to use as request body when sending a request.
     *
     * @return The data to use as request body.
     */
    @Override
    public final JSONObject getData() {

        return null;
    }

    /**
     * Retrieve a map to use as Query parameters. Can be null or empty;
     *
     * @return A map
     */
    public Map<String, String> getQueryParams() {

        return null;
    }

    /**
     * Retrieve the URL that will be used to send the request.
     *
     * @return An {@link URL} instance.
     *
     * @throws MalformedURLException
     *         Threw if the URL isn't valid.
     */
    @Override
    public final URL getUrl() throws MalformedURLException {

        String              url    = this.getTargetUrl();
        Map<String, String> params = Optional.ofNullable(this.getQueryParams()).orElse(new HashMap<>());

        if (params.size() > 0) {

            List<String> queryParams = new ArrayList<>();

            params.forEach((k, v) -> queryParams.add(this.enc(k) + "=" + this.enc(v)));

            url = url + "?" + String.join("&", queryParams);
        }

        return new URL(url);
    }

    /**
     * Utility method to enhance the code reading.
     *
     * @param input
     *         The string to encode
     *
     * @return The URL encoded string
     */
    private String enc(String input) {

        return URLEncoder.encode(input, StandardCharsets.UTF_8);
    }

}
