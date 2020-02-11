package fr.alexpado.bots.cmb.models.discord;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.repositories.DiscordChannelRepository;
import lombok.Getter;
import lombok.Setter;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Optional;

@Entity
@Getter
@Setter
public class DiscordChannel {

    @Id
    private Long id;

    @OneToOne
    private DiscordGuild discordGuild;

    private String name;

    private boolean announcement;

    @Column(length = 3)
    private String language;

    public static DiscordChannel fromJDAChannel(AppConfig config, TextChannel textChannel) {
        DiscordGuild guild = DiscordGuild.fromRefresh(config, textChannel.getGuild());
        DiscordChannel channel = new DiscordChannel();
        channel.setId(textChannel.getIdLong());
        channel.setDiscordGuild(guild);
        channel.setName(textChannel.getName());
        channel.setAnnouncement(false);
        channel.setLanguage(null);
        return channel;
    }

    public static DiscordChannel fromRefresh(AppConfig config, TextChannel channel) {
        DiscordChannelRepository repository = config.getDiscordChannelRepository();
        Optional<DiscordChannel> optionalDiscordChannel = repository.findById(channel.getIdLong());
        DiscordChannel discordChannel;
        if (!optionalDiscordChannel.isPresent()) {
            discordChannel = DiscordChannel.fromJDAChannel(config, channel);
        } else {
            discordChannel = optionalDiscordChannel.get();
            discordChannel.setName(channel.getName());
        }

        repository.save(discordChannel);
        return discordChannel;
    }

}
