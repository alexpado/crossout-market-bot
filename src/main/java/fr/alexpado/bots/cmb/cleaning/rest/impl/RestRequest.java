package fr.alexpado.bots.cmb.cleaning.rest.impl;

import fr.alexpado.bots.cmb.cleaning.rest.exceptions.RequestException;
import fr.alexpado.bots.cmb.cleaning.rest.interfaces.IRestRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Class allowing control over the bare minimum method possible when it comes to sending HTTP Request from implementing
 * classes.
 *
 * @param <T>
 *         The type of the entity returned by the REST API.
 *
 * @author alexpado
 */
public abstract class RestRequest<T> implements IRestRequest<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestRequest.class);

    /**
     * Execute the request in an async way.
     */
    @Override
    public final void queue() {

        this.queue((response) -> {});
    }

    /**
     * Execute the request in an async way.
     *
     * @param onSuccess
     *         The consumer that will be used when the request's response is ready.
     */
    @Override
    public final void queue(Consumer<T> onSuccess) {

        this.queue(onSuccess, (e) -> {
            if (e instanceof RequestException) {
                LOGGER.warn("The server responded with a non 2xx code. This is not handled by the caller.");
            } else {
                LOGGER.error("An unhandled exception occurred.", e);
            }
        });
    }

    /**
     * Execute the request in an async way.
     *
     * @param onSuccess
     *         The consumer that will be used when the request's response is ready.
     * @param onError
     *         The consumer that will be used if the request encounter an exception.
     */
    @Override
    public final void queue(Consumer<T> onSuccess, Consumer<Throwable> onError) {
        // Running the synchronous method in a Thread to be asynchronous.
        this.createThread(() -> {
            try {
                onSuccess.accept(this.complete());
            } catch (IOException e) {
                onError.accept(e);
            }
        });
    }

    /**
     * Create a {@link Thread} with the provided {@link Runnable} and executes it immediately.
     *
     * @param runnable
     *         The action to execute in the newly created {@link Thread}
     */
    private void createThread(Runnable runnable) {

        new Thread(runnable).start();
    }
}
