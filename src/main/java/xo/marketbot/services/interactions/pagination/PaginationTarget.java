package xo.marketbot.services.interactions.pagination;

import fr.alexpado.jda.interactions.entities.DispatchEvent;
import fr.alexpado.jda.interactions.enums.SlashTarget;
import fr.alexpado.jda.interactions.interfaces.interactions.Injection;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionResponseHandler;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionTarget;
import fr.alexpado.jda.interactions.interfaces.interactions.button.ButtonInteractionTarget;
import fr.alexpado.jda.interactions.meta.InteractionMeta;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonInteraction;
import xo.marketbot.responses.EntitiesDisplay;

import java.net.URI;
import java.util.Collections;
import java.util.Map;

public class PaginationTarget implements ButtonInteractionTarget {

    private final EntitiesDisplay<?> display;
    private final long               id;

    public PaginationTarget(EntitiesDisplay<?> display) {

        this.id      = System.currentTimeMillis();
        this.display = display;
    }

    @Override
    public InteractionMeta getMeta() {

        return new InteractionMeta("pagination://" + this.id, "", SlashTarget.ALL, Collections.emptyList(), false, false);
    }

    /**
     * Run this {@link InteractionTarget}.
     *
     * @param event
     *         The event responsible for this execution.
     * @param mapping
     *         Map of dependencies used for the parameter injection.
     *
     * @return An {@link Object} representing the result of the execution. The result can be used directly without any
     *         result handler.
     *
     * @see InteractionResponseHandler
     */
    @Override
    public Object execute(DispatchEvent<ButtonInteraction> event, Map<Class<?>, Injection<DispatchEvent<ButtonInteraction>, ?>> mapping) {

        String action = this.extractAction(event.getPath());

        switch (action.toLowerCase()) {
            case "next" -> this.display.next();
            case "previous" -> this.display.previous();
            default -> throw new IllegalStateException("Invalid action");
        }

        return this;
    }

    private String extractAction(URI uri) {

        String   query   = uri.getQuery();
        String[] options = query.split("&");

        for (String option : options) {
            String[] keyValue = option.split("=");
            if (keyValue[0].equals("action")) {
                return keyValue[1];
            }
        }
        throw new IllegalStateException("Unable to extract action.");
    }

    public EntitiesDisplay<?> getDisplay() {

        return display;
    }

    public long getId() {

        return id;
    }

}
