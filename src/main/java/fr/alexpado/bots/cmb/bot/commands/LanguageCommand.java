package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.interfaces.BotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import net.dv8tion.jda.api.entities.Message;

import java.util.List;

public class LanguageCommand extends BotCommand {

    public LanguageCommand(JDAModule module) {
        super(module, "language");
    }

    @Override
    public List<String> getLanguageKeys() {
        return null;
    }

    @Override
    public void execute(CommandEvent event, Message message) {

        // cm:language -> Show supported language
        // cm:language server <lang>
        // cm:language channel <lang>
        // cm:language user <lang>

    }
}
