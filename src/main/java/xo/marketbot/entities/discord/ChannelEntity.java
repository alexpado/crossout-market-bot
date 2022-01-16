package xo.marketbot.entities.discord;

import net.dv8tion.jda.api.entities.GuildChannel;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Optional;

/**
 * Entity class implementing the {@link ChannelEntity} interface.
 * <p>
 * This class is simply a wrapper for the {@link GuildChannel} interface, allowing it to hold some settings and to be save in a database.
 *
 * @author alexpado
 */
@Table(name = "channel")
@Entity
public class ChannelEntity {

    @Id
    private Long        id;
    @OneToOne
    private GuildEntity guild;
    private String      name;
    private String      language;

    /**
     * Create a new {@link ChannelEntity} with no data. This should not be used, and is present only for the sake of hibernate.
     */
    public ChannelEntity() {}

    /**
     * Create a new {@link ChannelEntity} with predefined data.
     *
     * @param channel
     *         The {@link GuildChannel} to use to initialize this instance.
     * @param guild
     *         The {@link GuildEntity} to which this instance belong.
     */
    public ChannelEntity(GuildChannel channel, GuildEntity guild) {

        this.id    = channel.getIdLong();
        this.name  = channel.getName();
        this.guild = guild;
    }

    public Long getId() {

        return id;
    }

    public void setId(Long id) {

        this.id = id;
    }

    public GuildEntity getGuild() {

        return guild;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getLanguage() {

        return language;
    }

    public void setLanguage(String language) {

        this.language = language;
    }

    /**
     * Retrieve the language to apply to this channel. If the language of the current channel is null, the guild's one will be returned.
     *
     * @return The effective language in this channel.
     */
    public String getEffectiveLanguage() {

        return Optional.ofNullable(this.getLanguage()).orElse(this.getGuild().getLanguage());
    }
}
