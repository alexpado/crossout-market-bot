package fr.alexpado.bots.cmb.cleaning.rest.impl;

import fr.alexpado.bots.cmb.cleaning.rest.exceptions.RequestException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

/**
 * Class representing a HTTP Request to a REST API.
 *
 * @param <T>
 *         The type of the entity returned by the REST API.
 *
 * @author alexpado
 */
public abstract class HttpRestRequest<T> extends RestRequest<T> {

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
    @Override
    public final T complete() throws IOException {

        HttpURLConnection connection = (HttpURLConnection) this.getConnection();
        this.beforeSend(connection);

        boolean     isOk     = connection.getResponseCode() >= 200 && connection.getResponseCode() <= 299;
        InputStream response = isOk ? connection.getInputStream() : connection.getErrorStream();

        // Read the response
        BufferedReader reader       = new BufferedReader(new InputStreamReader(response, StandardCharsets.UTF_8));
        String         responseText = reader.lines().collect(Collectors.joining("\n"));

        if (!isOk) {
            // The HTTP code isn't 2xx
            throw new RequestException(responseText, "The server did not respond with 2xx code.");
        }

        return this.convert(responseText);
    }

    /**
     * Retrieve the connection that will be used to send the request.
     *
     * @return An {@link URLConnection} implementation instance.
     */
    @Override
    public final URLConnection getConnection() throws IOException {

        return this.getUrl().openConnection();
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
    public URL getUrl() throws MalformedURLException {

        return new URL(this.getTargetUrl());
    }
}
