package xo.marketbot.responses;

import net.dv8tion.jda.api.JDA;

import java.awt.*;

public class EmptySearchEmbed extends DisplayTemplate {

    public EmptySearchEmbed(JDA jda) {

        super(jda);
        this.setDescription("Sorry, but nothing was found.");
        this.setColor(Color.ORANGE);
    }
}
