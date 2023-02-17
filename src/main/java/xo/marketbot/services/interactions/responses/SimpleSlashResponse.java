package xo.marketbot.services.interactions.responses;

import fr.alexpado.jda.interactions.responses.SlashResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageRequest;

import java.util.function.Consumer;

public class SimpleSlashResponse implements SlashResponse {

    private final EmbedBuilder builder;

    public SimpleSlashResponse(String message) {

        this.builder = new EmbedBuilder().setDescription(message);
    }

    public SimpleSlashResponse(String message, String imageUrl) {

        this.builder = new EmbedBuilder().setDescription(message).setImage(imageUrl);
    }

    public SimpleSlashResponse(EmbedBuilder builder) {

        this.builder = builder;
    }

    @Override
    public Consumer<MessageRequest<?>> getHandler() {

        return (amb) -> amb.setEmbeds(this.builder.build());
    }

    @Override
    public boolean isEphemeral() {

        return false;
    }

}
