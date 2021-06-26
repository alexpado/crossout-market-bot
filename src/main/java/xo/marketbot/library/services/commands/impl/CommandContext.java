package xo.marketbot.library.services.commands.impl;

import io.sentry.Sentry;
import io.sentry.SentryLevel;
import io.sentry.protocol.SentryId;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.repositories.ChannelEntityRepository;
import xo.marketbot.repositories.GuildEntityRepository;
import xo.marketbot.repositories.UserEntityRepository;

import java.util.Map;
import java.util.TreeMap;

public class CommandContext implements ICommandContext {

    private final GuildMessageReceivedEvent event;
    private final GuildEntityRepository     guildRepository;
    private final ChannelEntityRepository   channelRepository;
    private final UserEntityRepository      userRepository;
    private final GuildEntity               guild;
    private final ChannelEntity             channel;
    private final UserEntity                user;


    private CommandContext(GuildMessageReceivedEvent event, GuildEntityRepository guildRepository, ChannelEntityRepository channelRepository, UserEntityRepository userRepository) {

        this.event             = event;
        this.guildRepository   = guildRepository;
        this.channelRepository = channelRepository;
        this.userRepository    = userRepository;

        // Build context data

        long guildId   = this.getEvent().getGuild().getIdLong();
        long channelId = this.getEvent().getChannel().getIdLong();
        long userId    = this.getEvent().getAuthor().getIdLong();

        this.guild = this.guildRepository.findById(guildId).orElseGet(() -> {
            GuildEntity entity = new GuildEntity(this.getEvent().getGuild());
            return this.guildRepository.saveAndFlush(entity);
        });

        this.channel = this.channelRepository.findByIdAndGuild(channelId, this.guild).orElseGet(() -> {
            ChannelEntity entity = new ChannelEntity(this.getEvent().getChannel(), this.guild);
            return this.channelRepository.saveAndFlush(entity);
        });

        this.user = this.userRepository.findById(userId).orElseGet(() -> {
            UserEntity entity = new UserEntity(this.getEvent().getAuthor());
            return this.userRepository.saveAndFlush(entity);
        });

    }

    public static CommandContext createContext(GuildMessageReceivedEvent event, GuildEntityRepository guildRepository, ChannelEntityRepository channelRepository, UserEntityRepository userRepository) {

        return new CommandContext(event, guildRepository, channelRepository, userRepository);
    }

    /**
     * Retrieves the {@link GuildMessageReceivedEvent} that caused this context to be created.
     *
     * @return A {@link GuildMessageReceivedEvent}.
     */
    @Override
    public GuildMessageReceivedEvent getEvent() {

        return this.event;
    }

    /**
     * Retrieves the {@link GuildEntity} wrapping the {@link Guild} contained in {@link #getEvent()}.
     *
     * @return A {@link GuildEntity}.
     */
    @Override
    public GuildEntity getGuildEntity() {

        return this.guild;
    }

    /**
     * Retrieves the {@link ChannelEntity} wrapping the {@link TextChannel} contained in {@link #getEvent()}.
     *
     * @return A {@link ChannelEntity}.
     */
    @Override
    public ChannelEntity getChannelEntity() {

        return this.channel;
    }

    /**
     * Retrieves the {@link UserEntity} wrapping the {@link User} contained in {@link #getEvent()}.
     *
     * @return A {@link UserEntity}.
     */
    @Override
    public UserEntity getUserEntity() {

        return this.user;
    }

    /**
     * Retrieves the {@link GuildEntityRepository} allowing interaction with the database for {@link GuildEntity}.
     *
     * @return A {@link GuildEntityRepository}.
     */
    @Override
    public GuildEntityRepository getGuildRepository() {

        return this.guildRepository;
    }

    /**
     * Retrieves the {@link ChannelEntityRepository} allowing interaction with the database for {@link ChannelEntity}.
     *
     * @return A {@link ChannelEntityRepository}.
     */
    @Override
    public ChannelEntityRepository getChannelRepository() {

        return this.channelRepository;
    }

    /**
     * Retrieves the {@link UserEntityRepository} allowing interaction with the database for {@link UserEntity}.
     *
     * @return A {@link UserEntityRepository}.
     */
    @Override
    public UserEntityRepository getUserRepository() {

        return this.userRepository;
    }

    Map<String, String> asDiscordContext() {

        Map<String, String> context = new TreeMap<>();

        context.put("guild", this.getEvent().getGuild().getId());
        context.put("channel", this.getEvent().getChannel().getId());
        context.put("user", this.getEvent().getAuthor().getId());
        context.put("message", this.getEvent().getMessage().getId());
        context.put("content", this.getEvent().getMessage().getContentRaw());

        return context;
    }

    Map<String, String> asApplicationContext() {

        Map<String, String> context = new TreeMap<>();

        context.put("guild", String.format("[%s] %s", this.getGuildEntity().getLanguage(), this.getGuildEntity().getName()));
        context.put("channel", String.format("[%s] %s", this.getChannelEntity().getLanguage(), this.getChannelEntity().getName()));
        context.put("user", String.format("[%s] %s#%s", this.getUserEntity().getLanguage(), this.getUserEntity()
                                                                                                .getUsername(), this.getUserEntity()
                                                                                                                    .getDiscriminator()));

        return context;
    }

    public SentryId reportException(String label, Exception e) {

        Sentry.configureScope(scope -> {

            scope.setTag("command", label);
            scope.setContexts("discord", this.asDiscordContext());
            scope.setContexts("application", this.asApplicationContext());
            scope.setLevel(SentryLevel.ERROR);

            io.sentry.protocol.User user = new io.sentry.protocol.User();

            user.setId(String.valueOf(this.user.getId()));
            user.setUsername(this.user.getUsername() + "#" + this.user.getDiscriminator());

            scope.setUser(user);

        });


        return Sentry.captureException(e);
    }
}
