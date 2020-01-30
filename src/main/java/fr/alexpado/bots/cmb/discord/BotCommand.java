package fr.alexpado.bots.cmb.discord;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.crossout.models.Translation;
import fr.alexpado.bots.cmb.crossout.models.discord.DiscordGuild;
import fr.alexpado.bots.cmb.crossout.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.crossout.repositories.DiscordGuildRepository;
import fr.alexpado.bots.cmb.crossout.repositories.DiscordUserRepository;
import fr.alexpado.bots.cmb.crossout.repositories.TranslationRepository;
import fr.alexpado.bots.cmb.libs.jdamodules.JDAModule;
import fr.alexpado.bots.cmb.libs.jdamodules.commands.JDACommandExecutor;
import fr.alexpado.bots.cmb.libs.jdamodules.events.CommandEvent;
import fr.alexpado.bots.cmb.throwables.TranslationException;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public abstract class BotCommand extends JDACommandExecutor {

    private Map<String, String> translations = new HashMap<>();

    public BotCommand(JDAModule module, String label) {
        super(module, label);
    }

    public abstract List<String> getLanguageKeys();

    @Override
    public final void runCommand(CommandEvent event) {
        this.sendWaiting(event, message -> {
            DiscordGuildRepository dgr = this.getBot().getDiscordGuildRepository();
            DiscordUserRepository dur = this.getBot().getDiscordUserRepository();
            Optional<DiscordGuild> optionalDiscordGuild = dgr.findById(event.getGuild().getIdLong());
            DiscordGuild guild;
            if (!optionalDiscordGuild.isPresent()) {
                guild = DiscordGuild.fromJDAGuild(event.getGuild());
                dur.save(guild.getUser());
                dgr.save(guild);
            } else {
                guild = optionalDiscordGuild.get();
            }
            try {
                this.translations = this.getTranslations(guild.getLanguage());
                this.execute(event, message);
            } catch (TranslationException e) {
                this.sendError(message, "Unable to load translations : " + e.getMessage());
            }
        });
    }

    public abstract void execute(CommandEvent event, Message message);

    public DiscordBot getBot() {
        return ((DiscordBot) this.getModule().getBot());
    }

    private Map<String, String> getTranslations(String language) throws TranslationException {

        if (this.getLanguageKeys().size() == 0) {
            return new HashMap<>();
        }

        TranslationRepository repository = this.getConfig().translationRepository;
        List<Translation> translations = repository.getNeededFromLanguage(this.getLanguageKeys(), language);

        if (translations.size() != this.getLanguageKeys().size()) {
            throw new TranslationException(this.getLanguageKeys().size(), translations.size());
        }

        Map<String, String> translationsMap = new HashMap<>();
        translations.forEach(translation -> translationsMap.put(translation.getTranslationKey(), translation.getText()));
        return translationsMap;
    }

    public Map<String, String> getTranslations() {
        return this.translations;
    }

    public String getTranslation(String key) {
        return this.translations.get(key);
    }

    public DiscordUser getDiscordUser(CommandEvent event) {
        return this.getDiscordUser(event.getAuthor());
    }

    public DiscordUser getDiscordUser(User user) {
        DiscordUserRepository dur = this.getConfig().discordUserRepository;
        Optional<DiscordUser> optionalDiscordUser = dur.findById(user.getIdLong());
        DiscordUser discordUser;
        if (!optionalDiscordUser.isPresent()) {
            discordUser = DiscordUser.fromJDAUser(user);
            dur.save(discordUser);
        } else {
            discordUser = optionalDiscordUser.get();
        }
        return discordUser;
    }

    @Override
    public final String getDescription() {
        return String.format("command.%s.description", this.getLabel());
    }

    public final AppConfig getConfig() {
        return ((DiscordBot) this.getModule().getBot()).getConfig();
    }

}
