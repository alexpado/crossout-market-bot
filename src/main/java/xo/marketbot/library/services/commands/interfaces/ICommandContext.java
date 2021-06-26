package xo.marketbot.library.services.commands.interfaces;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.repositories.ChannelEntityRepository;
import xo.marketbot.repositories.GuildEntityRepository;
import xo.marketbot.repositories.UserEntityRepository;

public interface ICommandContext {

    /**
     * Retrieves the {@link GuildMessageReceivedEvent} that caused this context to be created.
     *
     * @return A {@link GuildMessageReceivedEvent}.
     */
    GuildMessageReceivedEvent getEvent();

    /**
     * Retrieves the {@link GuildEntity} wrapping the {@link Guild} contained in {@link #getEvent()}.
     *
     * @return A {@link GuildEntity}.
     */
    GuildEntity getGuildEntity();

    /**
     * Retrieves the {@link ChannelEntity} wrapping the {@link TextChannel} contained in {@link #getEvent()}.
     *
     * @return A {@link ChannelEntity}.
     */
    ChannelEntity getChannelEntity();

    /**
     * Retrieves the {@link UserEntity} wrapping the {@link User} contained in {@link #getEvent()}.
     *
     * @return A {@link UserEntity}.
     */
    UserEntity getUserEntity();

    /**
     * Retrieves the {@link GuildEntityRepository} allowing interaction with the database for {@link GuildEntity}.
     *
     * @return A {@link GuildEntityRepository}.
     */
    GuildEntityRepository getGuildRepository();

    /**
     * Retrieves the {@link ChannelEntityRepository} allowing interaction with the database for {@link ChannelEntity}.
     *
     * @return A {@link ChannelEntityRepository}.
     */
    ChannelEntityRepository getChannelRepository();

    /**
     * Retrieves the {@link UserEntityRepository} allowing interaction with the database for {@link UserEntity}.
     *
     * @return A {@link UserEntityRepository}.
     */
    UserEntityRepository getUserRepository();

}
