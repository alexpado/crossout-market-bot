package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.interfaces.command.TranslatableBotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.discord.DiscordChannel;
import fr.alexpado.bots.cmb.models.discord.DiscordGuild;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.models.game.Item;
import fr.alexpado.bots.cmb.repositories.TranslationRepository;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class LanguageCommand extends TranslatableBotCommand {

    private List<String> supportedLanguages;

    public LanguageCommand(JDAModule module) {
        super(module, "language");
    }

    @Override
    public List<String> getRequiredTranslation() {
        return Arrays.asList(
                Translation.GENERAL_BAD_SYNTAX,
                Translation.LANGUAGES_LIST,
                Translation.LANGUAGES_NOTSUPPORTED,
                Translation.LANGUAGES_CHANNEL_UPDATED,
                Translation.LANGUAGES_GUILD_UPDATED,
                Translation.LANGUAGES_USER_UPDATED
        );
    }

    @Override
    public void execute(CommandEvent event, Message message) {

        this.loadSupportedLanguages();

        if (event.getArgs().size() == 0) {
            new EmbedPage<String>(message, this.supportedLanguages, 10) {
                @Override
                public EmbedBuilder getEmbed() {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(LanguageCommand.this.getTranslation(Translation.LANGUAGES_LIST));
                    return builder;
                }
            };
            return;
        }

        if (event.getArgs().size() != 2) {
            this.sendWarn(message, this.getTranslation(Translation.GENERAL_BAD_SYNTAX));
            return;
        }

        String lang = event.getArgs().get(1);
        if (!this.isLanguageSupported(lang) && !lang.equals("remove")) {
            this.sendError(message, this.getTranslation(Translation.LANGUAGES_NOTSUPPORTED));
            return;
        }

        switch (event.getArgs().get(0)) {
            case "guild":
            case "server":
                if (lang.equals("remove")) {
                    this.sendError(message, this.getTranslation(Translation.LANGUAGES_NOTSUPPORTED));
                    return;
                }
                this.setServerLanguage(lang);
                this.sendInfo(message, this.getTranslation(Translation.LANGUAGES_GUILD_UPDATED));
                break;
            case "channel":
                this.setChannelLanguage(lang);
                this.sendInfo(message, this.getTranslation(Translation.LANGUAGES_CHANNEL_UPDATED));
                break;
            case "user":
                if (lang.equals("remove")) {
                    this.sendError(message, this.getTranslation(Translation.LANGUAGES_NOTSUPPORTED));
                    return;
                }
                this.setUserLanguage(lang);
                this.sendInfo(message, this.getTranslation(Translation.LANGUAGES_USER_UPDATED));
                break;
            default:
                this.sendWarn(message, this.getTranslation(Translation.GENERAL_BAD_SYNTAX));
        }
    }

    private void loadSupportedLanguages() {
        TranslationRepository repository = this.getConfig().getTranslationRepository();
        this.supportedLanguages = repository.supportedLanguages();
    }

    private boolean isLanguageSupported(String language) {
        return this.supportedLanguages.contains(language);
    }

    private void setServerLanguage(String lang) {
        DiscordGuild guild = this.getDiscordGuild();
        guild.setLanguage(lang);
        this.getConfig().getDiscordGuildRepository().save(guild);
    }

    private void setChannelLanguage(String lang) {
        DiscordChannel channel = this.getDiscordChannel();
        channel.setLanguage(lang.equals("remove") ? null : lang);
        this.getConfig().getDiscordChannelRepository().save(channel);
    }

    private void setUserLanguage(String lang) {
        DiscordUser user = this.getDiscordUser();
        user.setLanguage(lang);
        this.getConfig().getDiscordUserRepository().save(user);

        List<Watcher> watchers = this.getConfig().getWatcherRepository().getFromUser(user);
        List<Item> items = new ItemEndpoint(this.getConfig()).getAll(lang);

        HashMap<Integer, Item> itemsMap = new HashMap<>();
        items.forEach(item -> itemsMap.put(item.getId(), item));
        watchers.forEach(watcher -> watcher.setItemName(itemsMap.get(watcher.getId()).getAvailableName()));
        this.getConfig().getWatcherRepository().saveAll(watchers);
    }
}
