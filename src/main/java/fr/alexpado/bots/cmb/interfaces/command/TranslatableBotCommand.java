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

    private final CrossoutConfiguration     config;
    // Fake double extends
    private final Translatable              translatable;
    private       DiscordGuildRepository    discordGuildRepository;
    private       DiscordChannelRepository  discordChannelRepository;
    private       DiscordUserRepository     discordUserRepository;
    private       GuildSettingsRepository   guildSettingsRepository;
    private       ChannelSettingsRepository channelSettingsRepository;
    private       UserSettingsRepository    userSettingsRepository;
    private       DiscordGuild              discordGuild;
    private       DiscordUser               discordUser;
    private       DiscordChannel            discordChannel;
    private       GuildSettings             guildSettings;
    private       UserSettings              userSettings;
    private       ChannelSettings           channelSettings;
    private       String                    effectiveLanguage;

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

        this.discordGuildRepository   = accessor.getDiscordGuildRepository();
        this.discordUserRepository    = accessor.getDiscordUserRepository();
        this.discordChannelRepository = accessor.getDiscordChannelRepository();

        this.guildSettingsRepository   = accessor.getGuildSettingsRepository();
        this.userSettingsRepository    = accessor.getUserSettingsRepository();
        this.channelSettingsRepository = accessor.getChannelSettingsRepository();
    }

    private void loadObjects(CommandEvent event) {
        // Retrieve what we need
        this.discordUser    = this.discordUserRepository.findById(event.getAuthor().getIdLong())
                                                        .orElse(DiscordUser.getInstance(event.getAuthor()));
        this.discordGuild   = this.discordGuildRepository.findById(event.getGuild().getIdLong())
                                                         .orElse(DiscordGuild.getInstance(event.getGuild()));
        this.discordChannel = this.discordChannelRepository.findById(event.getChannel().getIdLong())
                                                           .orElse(DiscordChannel.getInstance(event.getChannel()));

        // Retrieve options, or create it.
        this.userSettings    = this.userSettingsRepository.findById(this.discordUser.getId())
                                                          .orElse(UserSettings.getInstance(this.discordUser));
        this.channelSettings = this.channelSettingsRepository.findById(this.discordChannel.getId())
                                                             .orElse(ChannelSettings.getInstance(this.discordChannel));
        this.guildSettings   = this.guildSettingsRepository.findById(this.discordGuild.getId())
                                                           .orElse(GuildSettings.getInstance(this.discordGuild));
    }

    private void saveAll() {

        this.discordUser    = this.discordUserRepository.save(this.discordUser);
        this.discordGuild   = this.discordGuildRepository.save(this.discordGuild);
        this.discordChannel = this.discordChannelRepository.save(this.discordChannel);

        this.userSettings    = this.userSettingsRepository.save(this.userSettings);
        this.channelSettings = this.channelSettingsRepository.save(this.channelSettings);
        this.guildSettings   = this.guildSettingsRepository.save(this.guildSettings);
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
                this.effectiveLanguage = this.getChannelSettings().getLanguage();
            } else {
                this.effectiveLanguage = this.getGuildSettings().getLanguage();
            }

            try {
                this.fetchTranslations(this.effectiveLanguage);
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

        return this.config;
    }

    public DiscordGuildRepository getDiscordGuildRepository() {

        return this.discordGuildRepository;
    }

    public DiscordChannelRepository getDiscordChannelRepository() {

        return this.discordChannelRepository;
    }

    public DiscordUserRepository getDiscordUserRepository() {

        return this.discordUserRepository;
    }

    public GuildSettingsRepository getGuildSettingsRepository() {

        return this.guildSettingsRepository;
    }

    public ChannelSettingsRepository getChannelSettingsRepository() {

        return this.channelSettingsRepository;
    }

    public UserSettingsRepository getUserSettingsRepository() {

        return this.userSettingsRepository;
    }

    public DiscordGuild getDiscordGuild() {

        return this.discordGuild;
    }

    public DiscordUser getDiscordUser() {

        return this.discordUser;
    }

    public DiscordChannel getDiscordChannel() {

        return this.discordChannel;
    }

    public GuildSettings getGuildSettings() {

        return this.guildSettings;
    }

    public UserSettings getUserSettings() {

        return this.userSettings;
    }

    public ChannelSettings getChannelSettings() {

        return this.channelSettings;
    }

    public String getEffectiveLanguage() {

        return this.effectiveLanguage;
    }

    @Override
    public final String getTranslation(String key) {

        return this.translatable.getTranslation(key);
    }

    @Override
    public final void fetchTranslations(String language) throws MissingTranslationException {

        this.translatable.fetchTranslations(language);
    }

    @Override
    public final List<Translation> fetch(List<String> keys, String language) throws MissingTranslationException {

        return this.translatable.fetch(keys, language);
    }

}
