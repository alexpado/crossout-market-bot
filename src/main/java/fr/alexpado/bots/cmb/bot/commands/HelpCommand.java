package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.bot.CrossoutModule;
import fr.alexpado.bots.cmb.bot.DiscordBot;
import fr.alexpado.bots.cmb.interfaces.command.TranslatableBotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.commands.JDACommandExecutor;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.repositories.TranslationRepository;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class HelpCommand extends TranslatableBotCommand {

    public HelpCommand(JDAModule module) {
        super(module, "help");
    }

    @Override
    public List<String> getRequiredTranslation() {
        return Arrays.asList(
                Translation.GENERAL_INVITE,
                Translation.COMMANDS_NOTFOUND,
                Translation.COMMANDS_NOHELP,
                Translation.HELP_DESCRIPTION
        );
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        if (event.getArgs().size() == 1) {
            Optional<JDACommandExecutor> optionalExecutor = this.getModule().getBot().getCommandManager().getCommand(event.getArgs().get(0));
            if (optionalExecutor.isPresent()) {
                EmbedBuilder builder = optionalExecutor.get().getAdvancedHelp();
                if (builder != null) {
                    message.editMessage(optionalExecutor.get().getAdvancedHelp().build()).queue();
                    return;
                }
                this.sendWarn(message, this.getTranslation(Translation.COMMANDS_NOHELP));
                return;
            }
            this.sendError(message, this.getTranslation(Translation.COMMANDS_NOTFOUND));
            return;
        }
        this.showHelp(event, message);
    }

    private void showHelp(CommandEvent event, Message message) {
        CrossoutModule crossout = this.getCrossoutModule();
        TranslationRepository repository = this.getConfig().getTranslationRepository();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(this.getTranslation(Translation.GENERAL_INVITE), DiscordBot.INVITE, event.getJDA().getSelfUser().getAvatarUrl());
        builder.setDescription(this.getTranslation(Translation.HELP_DESCRIPTION));

        HashMap<String, String> helpItems = new HashMap<>();

        for (JDACommandExecutor command : crossout.getBot().getCommandManager().getCommands()) {
            helpItems.put(command.getLabel(), command.getDescription());
        }

        List<Translation> translationList = repository.getNeededFromLanguage(helpItems.values(), this.getEffectiveLanguage());
        HashMap<String, String> translationMap = new HashMap<>();

        for (Translation translation : translationList) {
            translationMap.put(translation.getTranslationKey(), translation.getText());
        }

        helpItems.replaceAll((l, v) -> translationMap.get(helpItems.get(l)));
        helpItems.forEach((label, description) -> builder.addField(label, description, false));


        message.editMessage(builder.build()).queue();
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {
        return null;
    }
}
