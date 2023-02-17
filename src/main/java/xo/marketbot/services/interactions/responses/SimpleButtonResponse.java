package xo.marketbot.services.interactions.responses;

import fr.alexpado.jda.interactions.responses.ButtonResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageRequest;

import java.util.function.Consumer;

public class SimpleButtonResponse implements ButtonResponse {

    private final EmbedBuilder builder;

    public SimpleButtonResponse(String message) {

        this.builder = new EmbedBuilder().setDescription(message);
    }

    public SimpleButtonResponse(String message, String imageUrl) {

        this.builder = new EmbedBuilder().setDescription(message).setImage(imageUrl);
    }

    @Override
    public Consumer<MessageRequest<?>> getHandler() {

        return (amb) -> amb.setEmbeds(this.builder.build());
    }

    @Override
    public boolean shouldEditOriginalMessage() {

        return true;
    }

    @Override
    public boolean isEphemeral() {

        return false;
    }

}
