package fr.alexpado.bots.cmb.bot;

import fr.alexpado.bots.cmb.bot.commands.ItemCommand;
import fr.alexpado.bots.cmb.bot.commands.PackCommand;
import fr.alexpado.bots.cmb.bot.commands.watchers.WatchCommand;
import fr.alexpado.bots.cmb.bot.commands.watchers.WatcherSettingsCommand;
import fr.alexpado.bots.cmb.bot.commands.watchers.WatchlistCommand;
import fr.alexpado.bots.cmb.libs.jda.JDABot;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.commands.JDACommandExecutor;

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
        return Arrays.asList(
                new ItemCommand(this),
                new PackCommand(this),
                new WatchlistCommand(this),
                new WatchCommand(this),
                new WatcherSettingsCommand(this)
        );
    }
}
