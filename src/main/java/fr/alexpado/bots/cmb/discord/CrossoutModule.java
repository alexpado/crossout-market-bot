package fr.alexpado.bots.cmb.discord;

import fr.alexpado.bots.cmb.discord.commands.DebugCommand;
import fr.alexpado.bots.cmb.discord.commands.ItemCommand;
import fr.alexpado.bots.cmb.discord.commands.PackCommand;
import fr.alexpado.bots.cmb.discord.commands.WatcherCommand;
import fr.alexpado.bots.cmb.libs.jdamodules.JDABot;
import fr.alexpado.bots.cmb.libs.jdamodules.JDAModule;
import fr.alexpado.bots.cmb.libs.jdamodules.commands.JDACommandExecutor;

import java.util.Arrays;
import java.util.List;

public class CrossoutModule extends JDAModule {

    public CrossoutModule(JDABot bot) throws RuntimeException {
        super(bot);
    }

    @Override
    public String getName() {
        return "crossout";
    }

    @Override
    public List<JDACommandExecutor> getCommandExecutors() {
        return Arrays.asList(new ItemCommand(this), new PackCommand(this), new WatcherCommand(this), new DebugCommand(this));
    }
}
