package xo.marketbot.exceptions;

import fr.alexpado.jda.interactions.interfaces.DiscordEmbeddable;
import net.dv8tion.jda.api.EmbedBuilder;

import java.awt.*;

public class EmbedException extends RuntimeException implements DiscordEmbeddable {

    public EmbedException(String message) {

        super(message);
    }

    @Override
    public EmbedBuilder asEmbed() {

        return new EmbedBuilder().setDescription(this.getMessage()).setColor(Color.RED);
    }

    @Override
    public boolean showToEveryone() {

        return false;
    }

}
