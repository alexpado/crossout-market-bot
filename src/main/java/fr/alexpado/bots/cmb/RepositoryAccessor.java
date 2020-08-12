package fr.alexpado.bots.cmb;

import fr.alexpado.bots.cmb.modules.crossout.repositories.FakeItemRepository;
import fr.alexpado.bots.cmb.modules.crossout.repositories.OldTranslationRepository;
import fr.alexpado.bots.cmb.modules.crossout.repositories.OldWatcherRepository;
import fr.alexpado.bots.cmb.modules.crossout.repositories.settings.ChannelSettingsRepository;
import fr.alexpado.bots.cmb.modules.crossout.repositories.settings.GuildSettingsRepository;
import fr.alexpado.bots.cmb.modules.crossout.repositories.settings.UserSettingsRepository;
import fr.alexpado.bots.cmb.modules.discord.repositories.DiscordChannelRepository;
import fr.alexpado.bots.cmb.modules.discord.repositories.DiscordGuildRepository;
import fr.alexpado.bots.cmb.modules.discord.repositories.DiscordUserRepository;
import fr.alexpado.bots.cmb.modules.discord.repositories.SessionRepository;
import lombok.Getter;
import org.springframework.stereotype.Component;

@Component
@Getter
public class RepositoryAccessor {

    private final ChannelSettingsRepository channelSettingsRepository;
    private final GuildSettingsRepository   guildSettingsRepository;
    private final UserSettingsRepository    userSettingsRepository;
    private final FakeItemRepository        fakeItemRepository;
    private final OldTranslationRepository  translationRepository;
    private final OldWatcherRepository      watcherRepository;
    private final DiscordChannelRepository  discordChannelRepository;
    private final DiscordGuildRepository    discordGuildRepository;
    private final DiscordUserRepository     discordUserRepository;
    private final SessionRepository         sessionRepository;

    public RepositoryAccessor(ChannelSettingsRepository channelSettingsRepository, GuildSettingsRepository guildSettingsRepository, UserSettingsRepository userSettingsRepository, FakeItemRepository fakeItemRepository, OldTranslationRepository translationRepository, OldWatcherRepository watcherRepository, DiscordChannelRepository discordChannelRepository, DiscordGuildRepository discordGuildRepository, DiscordUserRepository discordUserRepository, SessionRepository sessionRepository) {

        this.channelSettingsRepository = channelSettingsRepository;
        this.guildSettingsRepository   = guildSettingsRepository;
        this.userSettingsRepository    = userSettingsRepository;
        this.fakeItemRepository        = fakeItemRepository;
        this.translationRepository     = translationRepository;
        this.watcherRepository         = watcherRepository;
        this.discordChannelRepository  = discordChannelRepository;
        this.discordGuildRepository    = discordGuildRepository;
        this.discordUserRepository     = discordUserRepository;
        this.sessionRepository         = sessionRepository;
    }

}
