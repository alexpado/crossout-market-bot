package fr.alexpado.bots.cmb.discord;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.libs.jdamodules.JDAModule;
import fr.alexpado.bots.cmb.libs.jdamodules.commands.JDACommandExecutor;

public abstract class BotCommand extends JDACommandExecutor {

    public BotCommand(JDAModule module, String label) {
        super(module, label);
    }

    public AppConfig getConfig() {
        return ((DiscordBot) this.getModule().getBot()).getConfig();
    }

}
