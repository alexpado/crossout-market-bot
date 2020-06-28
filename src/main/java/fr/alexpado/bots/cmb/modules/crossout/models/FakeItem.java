package fr.alexpado.bots.cmb.modules.crossout.models;

import fr.alexpado.bots.cmb.bot.DiscordBot;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.awt.*;
import java.util.Objects;

@Entity
@Getter
@Setter
public class FakeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
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

    @Override
    public boolean equals(Object o) {

        if (this == o) { return true; }
        if (!(o instanceof FakeItem)) { return false; }
        FakeItem fakeItem = (FakeItem) o;
        return this.id.equals(fakeItem.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.id);
    }

}
