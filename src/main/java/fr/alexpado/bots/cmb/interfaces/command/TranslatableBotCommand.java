package fr.alexpado.bots.cmb.interfaces.command;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.bot.CrossoutModule;
import fr.alexpado.bots.cmb.bot.DiscordBot;
import fr.alexpado.bots.cmb.interfaces.translatable.ITranslatable;
import fr.alexpado.bots.cmb.interfaces.translatable.Translatable;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.commands.JDACommandExecutor;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.discord.DiscordGuild;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.repositories.DiscordGuildRepository;
import fr.alexpado.bots.cmb.repositories.DiscordUserRepository;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;
import java.util.Objects;

public abstract class TranslatableBotCommand extends JDACommandExecutor implements ITranslatable {

    private DiscordGuild discordGuild;
    private DiscordUser discordUser;

    private AppConfig config;
    // Fake double extends
    private Translatable translatable;

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

    public void init(CommandEvent event, AppConfig config) {
        // Retrieve the repository
        DiscordGuildRepository discordGuildRepository = config.getDiscordGuildRepository();
        DiscordUserRepository discordUserRepository = config.getDiscordUserRepository();

        // Refreshing database
        this.discordUser = DiscordUser.fromRefresh(discordUserRepository, event.getAuthor());
        DiscordUser owner = DiscordUser.fromRefresh(discordUserRepository, Objects.requireNonNull(event.getGuild().getOwner()).getUser());
        this.discordGuild = DiscordGuild.fromRefresh(discordGuildRepository, event.getGuild(), owner);
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
            AppConfig config = ((DiscordBot) this.getModule().getBot()).getConfig();

            this.init(event, config);

            try {
                this.fetchTranslations(this.discordGuild.getLanguage());
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

    public AppConfig getConfig() {
        return config;
    }

    public DiscordGuild getDiscordGuild() {
        return discordGuild;
    }

    public DiscordUser getDiscordUser() {
        return discordUser;
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
