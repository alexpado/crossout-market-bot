package fr.alexpado.bots.cmb.crossout.models.discord;

import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity @Getter @Setter
public class DiscordUser {

    public static DiscordUser fromJDAUser(User user) {
        DiscordUser discordUser = new DiscordUser();

        discordUser.setId(user.getIdLong());
        discordUser.setName(user.getName());
        discordUser.setAvatarUrl(user.getEffectiveAvatarUrl());

        return discordUser;
    }

    @Id
    private long id;

    private String name;

    private String avatarUrl;

}
