package fr.alexpado.bots.cmb.models;

import fr.alexpado.bots.cmb.bot.DiscordBot;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.awt.*;

@Entity
public class FakeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    private String name;

    private String description;

    private String picture;


    public EmbedBuilder getAsEmbed(JDA jda, String generalInvite) {
        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(generalInvite, DiscordBot.INVITE, jda.getSelfUser().getAvatarUrl());
        builder.setTitle(this.name, "http://objection.lol/objection/29089");
        builder.setDescription(this.description);
        builder.setThumbnail(this.picture);
        builder.setColor(Color.PINK);

        return builder;
    }

}
