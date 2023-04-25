package xo.marketbot.helpers;

import fr.alexpado.jda.interactions.entities.DispatchEvent;
import fr.alexpado.jda.interactions.interfaces.DiscordEmbeddable;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionErrorHandler;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionResponseHandler;
import io.sentry.Sentry;
import io.sentry.protocol.SentryId;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;

public class ExceptionHandler implements InteractionErrorHandler {

    /**
     * Called when an exception occurs during the execution of an {@link Interaction}.
     *
     * @param event
     *         The {@link DispatchEvent} that was being executed when the exception was thrown.
     * @param exception
     *         The thrown {@link Exception}.
     */
    @Override
    public <T extends Interaction> void handleException(DispatchEvent<T> event, Exception exception) {

        SentryId sentryId = null;
        if (!(exception instanceof DiscordEmbeddable embeddable)) {
            sentryId = Sentry.captureException(exception); // Always report these.
        }

        if (event.getInteraction() instanceof IReplyCallback callback) {
            if (exception instanceof DiscordEmbeddable embeddable) {
                this.answer(callback, embeddable.asEmbed().build(), !embeddable.showToEveryone());
                return;
            }

            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("An error occurred.");
            builder.setDescription("""
                                           Something went wrong and we couldn't process your request. Please try again later.
                                                                              
                                           If the error persists, please contact the developer.
                                           """);

            builder.setFooter(String.format("I-Path: %s • ID: %s", event.getPath().toString(), sentryId));
            this.answer(callback, builder.build(), true);
        }

    }

    /**
     * Called when no {@link InteractionResponseHandler} could be found for the provided object.
     *
     * @param event
     *         The {@link DispatchEvent} that was used to generate the response.
     * @param response
     *         The response object generated.
     */
    @Override
    public <T extends Interaction> void onNoResponseHandlerFound(DispatchEvent<T> event, Object response) {

        if (event.getInteraction() instanceof IReplyCallback callback) {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setTitle("Oof, that's bad.");
            builder.setDescription("""
                                           Erm... What you did actually worked, but something went wrong when trying to display something back to you.
                                                                              
                                           Sorry for the inconvenience.""");

            builder.setFooter(String.format("I-Path: %s", event.getPath().toString()));
            this.answer(callback, builder.build(), true);
        }
    }

    private <T extends Interaction & IReplyCallback> void answer(T interaction, MessageEmbed embed, boolean ephemeral) {

        if (interaction.isAcknowledged()) {
            interaction.getHook().editOriginalEmbeds(embed).complete();
        } else {
            interaction.replyEmbeds(embed).setEphemeral(ephemeral).complete();
        }
    }

}
