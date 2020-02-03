package fr.alexpado.bots.cmb.models.discord;

import fr.alexpado.bots.cmb.bot.DiscordBot;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class DiscordUser {

    @Column(length = 3)
    private String language;

    @Id
    private long id;

    private String name;

    private String avatarUrl;

    public static DiscordUser fromJDAUser(User user) {

        DiscordUser discordUser = new DiscordUser();

        discordUser.setId(user.getIdLong());
        discordUser.setName(user.getName());
        discordUser.setAvatarUrl(user.getEffectiveAvatarUrl());
        discordUser.setLanguage(DiscordBot.getInstance().getConfig().getDefaultLocale());

        return discordUser;
    }

}
