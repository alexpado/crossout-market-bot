package fr.alexpado.bots.cmb.entities.i18n.messages;

import fr.alexpado.bots.cmb.tools.DiscordEmbed;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;

import java.awt.*;

public class FatalErrorMessage extends DiscordEmbed {


    public FatalErrorMessage(JDA jda) {

        super(jda);
    }

    @NotNull
    @Override
    public MessageEmbed build() {

        super.setColor(Color.RED);
        super.setDescription("Oof, something very wrong happened here...");
        return super.build();
    }
}
