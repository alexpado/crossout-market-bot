package xo.marketbot.entities.discord;

import jakarta.persistence.*;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.middleman.GuildChannel;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;

/**
 * Entity class implementing the {@link ChannelEntity} interface.
 * <p>
 * This class is simply a wrapper for the {@link GuildChannel} interface, allowing it to hold some settings and to be
 * saved in a database.
 */
@Table(name = "channel")
@Entity
public class ChannelEntity {

    @Id
    private Long        id;
    @OneToOne
    private GuildEntity guild;
    private String      name;
    @ManyToOne
    private Language    language;

    /**
     * Create a new {@link ChannelEntity} with no data. This should not be used, and is present only for the sake of
     * hibernate.
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

    /**
     * Retrieve this {@link ChannelEntity}'s ID.
     *
     * @return The ID
     */
    public Long getId() {

        return this.id;
    }

    /**
     * Retrieve the {@link GuildEntity} to which this {@link ChannelEntity} belongs.
     *
     * @return A {@link GuildEntity}.
     */
    public GuildEntity getGuild() {

        return this.guild;
    }

    /**
     * Retrieve this {@link ChannelEntity}'s name.
     *
     * @return The name.
     */
    public String getName() {

        return this.name;
    }

    /**
     * Define this {@link ChannelEntity}'s name.
     *
     * @param name
     *         The name.
     */
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Retrieve this {@link ChannelEntity}'s language.
     *
     * @return The language.
     */
    @Nullable
    public Language getLanguage() {

        return this.language;
    }

    /**
     * Define this {@link ChannelEntity}'s language.
     *
     * @param language
     *         The language.
     */
    public void setLanguage(@Nullable Language language) {

        this.language = language;
    }

    /**
     * Retrieve the language to apply to this channel. If the language of the current channel is null, the guild's one
     * will be returned.
     *
     * @return The effective language in this channel.
     */
    public Language getEffectiveLanguage() {

        return Optional.ofNullable(this.getLanguage()).orElse(this.getGuild().getLanguage());
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof ChannelEntity channel) {
            return this.getId().equals(channel.getId());
        } else if (o instanceof Channel channel) {
            return this.getId().equals(channel.getIdLong());
        }
        return false;
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.getId());
    }

}
