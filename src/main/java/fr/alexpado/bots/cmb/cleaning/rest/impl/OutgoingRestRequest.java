package fr.alexpado.bots.cmb.cleaning.rest.impl;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * Class representing a HTTP Request to a REST API, with a request body.
 *
 * @param <T>
 *         The type of the entity returned by the REST API.
 *
 * @author alexpado
 */
public abstract class OutgoingRestRequest<T> extends HttpRestRequest<T> {

    /**
     * This method will be called before sending the request to the REST API. This method can be used to change
     * request's properties, like headers.
     *
     * @param connection
     *         The connection that will be used to do the request.
     */
    @Override
    public void beforeSend(URLConnection connection) throws IOException {

        HttpsURLConnection httpURLConnection = (HttpsURLConnection) connection;

        httpURLConnection.setRequestMethod(this.getMethod());
        httpURLConnection.setRequestProperty("Content-Type", "application/json");
        httpURLConnection.setRequestProperty("Accept", "application/json");
        httpURLConnection.setDoOutput(true);

        try (OutputStream os = httpURLConnection.getOutputStream()) {
            byte[] input = this.getData().toString().getBytes(StandardCharsets.UTF_8);
            os.write(input, 0, input.length);
        }
    }

    /**
     * Retrieve the method to execute this request.
     *
     * @return One of "POST", "PUT", "PATCH" or "DELETE"
     */
    protected abstract String getMethod();

    @Override
    public Map<String, String> getQueryParams() {

        return null;
    }
}
