package fr.alexpado.bots.cmb.interfaces.command;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.RepositoryAccessor;
import fr.alexpado.bots.cmb.bot.CrossoutModule;
import fr.alexpado.bots.cmb.bot.DiscordBot;
import fr.alexpado.bots.cmb.interfaces.translatable.ITranslatable;
import fr.alexpado.bots.cmb.interfaces.translatable.Translatable;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.commands.JDACommandExecutor;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.modules.crossout.models.settings.ChannelSettings;
import fr.alexpado.bots.cmb.modules.crossout.models.settings.GuildSettings;
import fr.alexpado.bots.cmb.modules.crossout.models.settings.UserSettings;
import fr.alexpado.bots.cmb.modules.crossout.repositories.settings.ChannelSettingsRepository;
import fr.alexpado.bots.cmb.modules.crossout.repositories.settings.GuildSettingsRepository;
import fr.alexpado.bots.cmb.modules.crossout.repositories.settings.UserSettingsRepository;
import fr.alexpado.bots.cmb.modules.discord.models.DiscordChannel;
import fr.alexpado.bots.cmb.modules.discord.models.DiscordGuild;
import fr.alexpado.bots.cmb.modules.discord.models.DiscordUser;
import fr.alexpado.bots.cmb.modules.discord.repositories.DiscordChannelRepository;
import fr.alexpado.bots.cmb.modules.discord.repositories.DiscordGuildRepository;
import fr.alexpado.bots.cmb.modules.discord.repositories.DiscordUserRepository;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public abstract class TranslatableBotCommand extends JDACommandExecutor implements ITranslatable {

    private final CrossoutConfiguration config;
    // Fake double extends
    private final Translatable translatable;
    private DiscordGuildRepository discordGuildRepository;
    private DiscordChannelRepository discordChannelRepository;
    private DiscordUserRepository discordUserRepository;
    private GuildSettingsRepository guildSettingsRepository;
    private ChannelSettingsRepository channelSettingsRepository;
    private UserSettingsRepository userSettingsRepository;
    private DiscordGuild discordGuild;
    private DiscordUser discordUser;
    private DiscordChannel discordChannel;
    private GuildSettings guildSettings;
    private UserSettings userSettings;
    private ChannelSettings channelSettings;
    private String effectiveLanguage;

    public TranslatableBotCommand(JDAModule module, String label) {
        super(module, label);

        this.config = ((DiscordBot) this.getModule().getBot()).getConfig();

        // Fake double extends
        this.translatable = new Translatable(this.config) {
            @Override
            public List<String> getRequiredTranslation() {
                return TranslatableBotCommand.this.getRequiredTranslation();
            }
        };
    }

    private void loadRepositories(CrossoutConfiguration config) {
        RepositoryAccessor accessor = config.getRepositoryAccessor();

        this.discordGuildRepository = accessor.getDiscordGuildRepository();
        this.discordUserRepository = accessor.getDiscordUserRepository();
        this.discordChannelRepository = accessor.getDiscordChannelRepository();

        this.guildSettingsRepository = accessor.getGuildSettingsRepository();
        this.userSettingsRepository = accessor.getUserSettingsRepository();
        this.channelSettingsRepository = accessor.getChannelSettingsRepository();
    }

    private void loadObjects(CommandEvent event) {
        // Retrieve what we need
        this.discordUser = DiscordUser.getInstance(event.getAuthor());
        this.discordGuild = DiscordGuild.getInstance(event.getGuild());
        this.discordChannel = DiscordChannel.getInstance(event.getChannel());

        // Retrieve options, or create it.
        this.userSettings = userSettingsRepository.findById(this.discordUser.getId()).orElse(UserSettings.getInstance(this.discordUser));
        this.channelSettings = channelSettingsRepository.findById(this.discordChannel.getId()).orElse(ChannelSettings.getInstance(this.discordChannel));
        this.guildSettings = guildSettingsRepository.findById(this.discordGuild.getId()).orElse(GuildSettings.getInstance(this.discordGuild));
    }

    private void saveAll() {
        this.discordUser = discordUserRepository.save(this.discordUser);
        this.discordGuild = discordGuildRepository.save(this.discordGuild);
        this.discordChannel = discordChannelRepository.save(this.discordChannel);

        this.userSettings = userSettingsRepository.save(this.userSettings);
        this.channelSettings = channelSettingsRepository.save(this.channelSettings);
        this.guildSettings = guildSettingsRepository.save(this.guildSettings);
    }

    public void init(CommandEvent event, CrossoutConfiguration config) {
        // Retrieve the repository
        this.loadRepositories(config);

        // Retrieve what we need
        this.loadObjects(event);

        // Saving everything
        this.saveAll();
    }

    /**
     * Get the translation key representing this command's description.
     *
     * @return A translation key.
     */
    @Override
    public final String getDescription() {
        return String.format("command.%s.description", this.getLabel());
    }

    public CrossoutModule getCrossoutModule() {
        return (CrossoutModule) this.getModule();
    }

    @Override
    public void runCommand(CommandEvent event) {
        this.sendWaiting(event, message -> {
            CrossoutConfiguration config = ((DiscordBot) this.getModule().getBot()).getConfig();

            this.init(event, config);

            if (this.getChannelSettings().getLanguage() != null) {
                effectiveLanguage = this.getChannelSettings().getLanguage();
            } else {
                effectiveLanguage = this.getGuildSettings().getLanguage();
            }

            try {
                this.fetchTranslations(effectiveLanguage);
            } catch (MissingTranslationException e) {
                // Cancel the command execution if at least one translation is missing
                this.sendError(message, e.getMessage());
                return;
            }

            try {
                // Run the command
                this.execute(event, message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public abstract void execute(CommandEvent event, Message message) throws Exception;

    public CrossoutConfiguration getConfig() {
        return config;
    }

    public DiscordGuildRepository getDiscordGuildRepository() {
        return discordGuildRepository;
    }

    public DiscordChannelRepository getDiscordChannelRepository() {
        return discordChannelRepository;
    }

    public DiscordUserRepository getDiscordUserRepository() {
        return discordUserRepository;
    }

    public GuildSettingsRepository getGuildSettingsRepository() {
        return guildSettingsRepository;
    }

    public ChannelSettingsRepository getChannelSettingsRepository() {
        return channelSettingsRepository;
    }

    public UserSettingsRepository getUserSettingsRepository() {
        return userSettingsRepository;
    }

    public DiscordGuild getDiscordGuild() {
        return discordGuild;
    }

    public DiscordUser getDiscordUser() {
        return discordUser;
    }

    public DiscordChannel getDiscordChannel() {
        return discordChannel;
    }

    public GuildSettings getGuildSettings() {
        return guildSettings;
    }

    public UserSettings getUserSettings() {
        return userSettings;
    }

    public ChannelSettings getChannelSettings() {
        return channelSettings;
    }

    public String getEffectiveLanguage() {
        return effectiveLanguage;
    }

    @Override
    public final String getTranslation(String key) {
        return translatable.getTranslation(key);
    }

    @Override
    public final void fetchTranslations(String language) throws MissingTranslationException {
        translatable.fetchTranslations(language);
    }

    @Override
    public final List<Translation> fetch(List<String> keys, String language) throws MissingTranslationException {
        return translatable.fetch(keys, language);
    }

}
