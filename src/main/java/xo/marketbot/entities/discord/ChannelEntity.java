package xo.marketbot.entities.discord;

import net.dv8tion.jda.api.entities.GuildChannel;
import xo.marketbot.interfaces.common.Identifiable;
import xo.marketbot.interfaces.common.LanguageHolder;
import xo.marketbot.interfaces.common.Nameable;
import xo.marketbot.interfaces.common.Ownable;
import xo.marketbot.interfaces.discord.IChannelEntity;
import xo.marketbot.interfaces.discord.IGuildEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

/**
 * Entity class implementing the {@link IChannelEntity} interface.
 * <p>
 * This class is simply a wrapper for the {@link GuildChannel} interface, allowing it to hold some settings and to be
 * save in a database.
 *
 * @author alexpado
 */
@Entity
public class ChannelEntity implements IChannelEntity {

    @Id
    private Long        id;
    @OneToOne
    private GuildEntity guild;
    private String      name;
    private String      language;

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
     * Retrieve this {@link Identifiable}'s ID.
     *
     * @return An ID.
     */
    @Override
    public Long getId() {

        return this.id;
    }

    /**
     * Retrieve this {@link Nameable}'s name.
     *
     * @return The name.
     */
    @Override
    public String getName() {

        return this.name;
    }

    @Override
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Retrieve this {@link Ownable}'s owner.
     *
     * @return The owner.
     */
    @Override
    public IGuildEntity getOwner() {

        return this.guild;
    }

    /**
     * Retrieve this {@link LanguageHolder}'s language. The language is usually represented by two characters.
     *
     * @return This {@link LanguageHolder}'s language.
     */
    @Override
    public String getLanguage() {

        return this.language;
    }

    /**
     * Define this {@link LanguageHolder}'s language. The language is usually represented by two characters.
     *
     * @param language
     *         This {@link LanguageHolder}'s language.
     */
    @Override
    public void setLanguage(String language) {

        this.language = language;
    }

    /**
     * Retrieve the language to apply to this channel. If the language of the current channel is null, the guild's one
     * will be returned.
     *
     * @return The effective language in this channel.
     */
    public String getEffectiveLanguage() {

        if (this.getLanguage() == null) {
            return this.getOwner().getLanguage();
        }
        return this.getLanguage();
    }
}
