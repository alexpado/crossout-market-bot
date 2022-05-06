package xo.marketbot.services.interactions.responses;

import fr.alexpado.jda.interactions.responses.ButtonResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

public class SimpleButtonResponse implements ButtonResponse {

    private final MessageBuilder builder;
    private final boolean        editOriginal;

    public SimpleButtonResponse(String message) {

        this(new EmbedBuilder().setDescription(message));
    }

    public SimpleButtonResponse(String message, String imageUrl) {

        this(new EmbedBuilder().setDescription(message).setImage(imageUrl));
    }

    public SimpleButtonResponse(EmbedBuilder builder) {

        this.builder = new MessageBuilder();
        this.builder.setEmbeds(builder.build());
        this.editOriginal = false;
    }

    @Override
    public Message getMessage() {

        return this.builder.build();
    }

    @Override
    public boolean shouldEditOriginalMessage() {

        return this.editOriginal;
    }

}
