package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.interfaces.command.TranslatableBotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public class TranslateCommand extends TranslatableBotCommand {

    public TranslateCommand(JDAModule module) {
        super(module, "translate");
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {
        return null;
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setDescription("For now, the translation website isn't available. If you wish to help translating the bot, please get in touch with `Akio Nakao#0001`");
        message.editMessage(builder.build()).queue();
    }

    @Override
    public List<String> getRequiredTranslation() {
        return new ArrayList<>();
    }
}
