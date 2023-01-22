package xo.marketbot.services.interactions.pagination;

import fr.alexpado.jda.interactions.entities.DispatchEvent;
import fr.alexpado.jda.interactions.impl.interactions.button.ButtonInteractionContainerImpl;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionResponseHandler;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionTarget;
import fr.alexpado.jda.interactions.interfaces.interactions.button.ButtonInteractionTarget;
import fr.alexpado.jda.interactions.responses.SlashResponse;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.callbacks.IReplyCallback;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageEditBuilder;
import org.jetbrains.annotations.Nullable;
import xo.marketbot.responses.EntitiesDisplay;
import xo.marketbot.services.interactions.responses.SimpleSlashResponse;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PaginationHandler extends ButtonInteractionContainerImpl {

    private final Map<String, PaginationTarget> paginationTargetMap = new HashMap<>();

    /**
     * Try to find an {@link InteractionTarget} matching the {@link URI}.
     *
     * @param uri
     *         The {@link URI} to match.
     *
     * @return An optional {@link InteractionTarget}
     */
    @Override
    public Optional<ButtonInteractionTarget> resolve(URI uri) {

        if (uri.getScheme().equals("pagination")) {
            return Optional.ofNullable(this.paginationTargetMap.get(uri.getHost()));
        }
        return super.resolve(uri);
    }

    /**
     * Called when an {@link DispatchEvent} is being fired.
     *
     * @param event
     *         The {@link DispatchEvent}
     *
     * @return An object representing the interaction result.
     */
    @Override
    public Object dispatch(DispatchEvent<ButtonInteraction> event) throws Exception {

        if (event.getPath().getScheme().equals("pagination")) {
            Optional<ButtonInteractionTarget> optionalTarget = this.resolve(event.getPath());
            if (optionalTarget.isEmpty()) {
                return new SimpleSlashResponse("Sorry, but this message can't be changed anymore");
            }
            return optionalTarget.get().execute(event, new HashMap<>());
        }
        return super.dispatch(event);
    }

    /**
     * Check if this {@link InteractionResponseHandler} can handle the provided response.
     *
     * @param event
     *         The {@link DispatchEvent} source of the response.
     * @param response
     *         The object representing the response given by an interaction.
     *
     * @return True if able to handle, false otherwise.
     */
    @Override
    public <T extends Interaction> boolean canHandle(DispatchEvent<T> event, @Nullable Object response) {

        return response instanceof PaginationTarget || super.canHandle(event, response);
    }

    /**
     * Handle the response resulting from the {@link DispatchEvent} event provided.
     *
     * @param event
     *         The {@link DispatchEvent} source of the response.
     * @param response
     *         The {@link Object} to handle.
     */
    @Override
    public <T extends Interaction> void handleResponse(DispatchEvent<T> event, @Nullable Object response) {

        if (response instanceof PaginationTarget target) {
            this.paginationTargetMap.put(String.valueOf(target.getId()), target);
            EntitiesDisplay<?> display = target.getDisplay();
            display.setTransactionId(target.getId());

            if (event.getInteraction() instanceof IReplyCallback callback) {
                if (callback.isAcknowledged()) {
                    event.getTimedAction().action("build", "Building the response");
                    MessageEditBuilder builder = this.getMessageEditBuilder(display);
                    event.getTimedAction().endAction();

                    event.getTimedAction().action("reply", "Replying to the interaction (EDIT)");
                    callback.getHook().editOriginal(builder.build()).complete();
                    event.getTimedAction().endAction();
                } else {
                    event.getTimedAction().action("build", "Building the response");
                    MessageCreateBuilder builder = this.getMessageCreateBuilder(display);
                    event.getTimedAction().endAction();

                    event.getTimedAction().action("reply", "Replying to the interaction (CREATE)");
                    callback.reply(builder.build()).setEphemeral(display.isEphemeral()).complete();
                    event.getTimedAction().endAction();
                }
            }
        }

        super.handleResponse(event, response);
    }

    private MessageEditBuilder getMessageEditBuilder(SlashResponse response) {

        MessageEditBuilder builder = new MessageEditBuilder();
        response.getHandler().accept(builder);
        return builder;
    }

    private MessageCreateBuilder getMessageCreateBuilder(SlashResponse response) {

        MessageCreateBuilder builder = new MessageCreateBuilder();
        response.getHandler().accept(builder);
        return builder;
    }

}
