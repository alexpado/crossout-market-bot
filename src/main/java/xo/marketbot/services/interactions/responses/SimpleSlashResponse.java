package xo.marketbot.services.interactions.responses;

import fr.alexpado.jda.interactions.responses.SlashResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;

public class SimpleSlashResponse implements SlashResponse {

    private final MessageBuilder builder;

    public SimpleSlashResponse(String message) {

        this(new EmbedBuilder().setDescription(message));
    }

    public SimpleSlashResponse(String message, String imageUrl) {

        this(new EmbedBuilder().setDescription(message).setImage(imageUrl));
    }

    public SimpleSlashResponse(EmbedBuilder builder) {

        this.builder = new MessageBuilder();
        this.builder.setEmbeds(builder.build());
    }

    @Override
    public Message getMessage() {

        return this.builder.build();
    }

    @Override
    public boolean isEphemeral() {

        return false;
    }

}
