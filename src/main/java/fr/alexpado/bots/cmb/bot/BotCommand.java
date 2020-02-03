package fr.alexpado.bots.cmb.bot;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.commands.JDACommandExecutor;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.discord.DiscordGuild;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.repositories.DiscordGuildRepository;
import fr.alexpado.bots.cmb.repositories.DiscordUserRepository;
import fr.alexpado.bots.cmb.repositories.TranslationRepository;
import fr.alexpado.bots.cmb.throwables.TranslationException;
import net.dv8tion.jda.api.entities.Guild;
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
                e.printStackTrace();
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

    public DiscordGuild getDiscordGuild(CommandEvent event) {
        return this.getDiscordGuild(event.getGuild());
    }

    public DiscordGuild getDiscordGuild(Guild guild) {
        DiscordGuildRepository dgr = this.getConfig().discordGuildRepository;
        Optional<DiscordGuild> optionalDiscordGuild = dgr.findById(guild.getIdLong());
        DiscordGuild discordGuild;
        if (!optionalDiscordGuild.isPresent()) {
            discordGuild = DiscordGuild.fromJDAGuild(guild);
            dgr.save(discordGuild);
        } else {
            discordGuild = optionalDiscordGuild.get();
        }
        return discordGuild;
    }


    @Override
    public final String getDescription() {
        return String.format("command.%s.description", this.getLabel());
    }

    public final AppConfig getConfig() {
        return ((DiscordBot) this.getModule().getBot()).getConfig();
    }

}
