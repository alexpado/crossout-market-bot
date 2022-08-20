package xo.marketbot.services.interactions.pagination;

import fr.alexpado.jda.interactions.entities.DispatchEvent;
import fr.alexpado.jda.interactions.impl.interactions.button.ButtonInteractionContainerImpl;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionResponseHandler;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionTarget;
import fr.alexpado.jda.interactions.interfaces.interactions.button.ButtonInteractionTarget;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.Interaction;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import org.jetbrains.annotations.Nullable;
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

            event.getTimedAction().action("build", "Building the response");
            MessageEmbed embed      = target.getDisplay().render().build();
            ActionRow[]  actionRows = target.getDisplay().getActionRows(String.valueOf(target.getId()));
            Message      message    = new MessageBuilder().setEmbeds(embed).setActionRows(actionRows).build();
            event.getTimedAction().endAction();

            event.getTimedAction().action("replying", "Sending the reply");
            if (event.getInteraction() instanceof ButtonInteraction button) {
                if (button.isAcknowledged()) {
                    button.getHook().editOriginal(message).complete();
                } else {
                    button.editMessage(message).complete();
                }
                return;
            } else if (event.getInteraction() instanceof SlashCommandInteraction slash) {
                if (slash.isAcknowledged()) {
                    slash.getHook().editOriginal(message).complete();
                } else {
                    slash.reply(message).complete();
                }
                return;
            }
            event.getTimedAction().endAction();
        }

        super.handleResponse(event, response);
    }

}
