package fr.alexpado.bots.cmb.models.discord;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.repositories.DiscordUserRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.User;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Optional;

@Entity
@Getter
@Setter
public class DiscordUser {

    @Id
    private long id;

    private String name;

    private String avatarUrl;

    @Column(length = 3)
    private String language;

    private boolean watcherPaused;

    public static DiscordUser fromJDAUser(AppConfig config, User user) {

        DiscordUser discordUser = new DiscordUser();

        discordUser.setId(user.getIdLong());
        discordUser.setName(user.getName());
        discordUser.setAvatarUrl(user.getEffectiveAvatarUrl());
        discordUser.setLanguage(config.getDefaultLocale());
        discordUser.setWatcherPaused(false);

        return discordUser;
    }

    public static DiscordUser fromRefresh(AppConfig config, User user) {
        DiscordUserRepository repository = config.getDiscordUserRepository();
        Optional<DiscordUser> optionalDiscordUser = repository.findById(user.getIdLong());
        DiscordUser discordUser;
        if (!optionalDiscordUser.isPresent()) {
            discordUser = DiscordUser.fromJDAUser(config, user);
        } else {
            discordUser = optionalDiscordUser.get();
            discordUser.setName(user.getName());
            discordUser.setAvatarUrl(user.getEffectiveAvatarUrl());
        }
        repository.save(discordUser);
        return discordUser;
    }

}
