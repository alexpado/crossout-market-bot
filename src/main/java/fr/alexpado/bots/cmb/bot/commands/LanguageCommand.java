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
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpBuilder;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpSection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
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

        boolean isOwner = event.getMember().isOwner();
        boolean isAdmin = event.getMember().getPermissions().contains(Permission.ADMINISTRATOR);
        boolean canManageChannel = event.getMember().getPermissions().contains(Permission.MANAGE_CHANNEL);
        boolean canManageThisChannel = event.getMember().getPermissions(event.getChannel()).contains(Permission.MANAGE_CHANNEL);

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
            this.sendWarn(message, String.format(this.getTranslation(Translation.GENERAL_BAD_SYNTAX), this.getLabel()));
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
                if (!isOwner || !isAdmin) {
                    this.sendError(message, this.getTranslation(Translation.GENERAL_FORBIDDEN));
                    return;
                }

                if (lang.equals("remove")) {
                    this.sendError(message, this.getTranslation(Translation.LANGUAGES_NOTSUPPORTED));
                    return;
                }
                this.setServerLanguage(lang);
                this.sendInfo(message, this.getTranslation(Translation.LANGUAGES_GUILD_UPDATED));
                break;
            case "channel":
                if (!isOwner || !isAdmin || !canManageChannel || !canManageThisChannel) {
                    this.sendError(message, this.getTranslation(Translation.GENERAL_FORBIDDEN));
                    return;
                }

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
        watchers.forEach(watcher -> watcher.setItemName(itemsMap.get(watcher.getItemId()).getAvailableName()));
        this.getConfig().getWatcherRepository().saveAll(watchers);
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {
        EmbedBuilder builder = super.getAdvancedHelp();
        builder.setTitle("Advanced help for : language");

        AdvancedHelpBuilder helpBuilder = new AdvancedHelpBuilder();

        helpBuilder.setDescription(this.getTranslation(this.getDescription()));

        AdvancedHelpSection usages = new AdvancedHelpSection("Usage");
        usages.addField("**`cm:language <server|guild|channel|user> <language|remove>`**", "Set the language for the desired target. 'Remove' is only available on channels.");

        AdvancedHelpSection examples = new AdvancedHelpSection("Example");
        examples.addField("`cm:language server en`", "Set the server language to english.");
        examples.addField("`cm:language user ru`", "Set your own language to russian.");
        examples.addField("`cm:language channel remove`", "Remove the current language defined on this channel. (Will use the server's language)");

        helpBuilder.addSection(usages);
        helpBuilder.addSection(examples);

        builder.setDescription(helpBuilder.toString());
        return builder;
    }

}
