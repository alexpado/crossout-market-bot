package fr.alexpado.bots.cmb.discord.commands;

import fr.alexpado.bots.cmb.crossout.models.Watcher;
import fr.alexpado.bots.cmb.crossout.repositories.WatcherRepository;
import fr.alexpado.bots.cmb.discord.BotCommand;
import fr.alexpado.bots.cmb.libs.jdamodules.JDAModule;
import fr.alexpado.bots.cmb.libs.jdamodules.events.CommandEvent;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.List;

public class WatcherCommand extends BotCommand {

    private WatcherRepository repository;

    public WatcherCommand(JDAModule module) {
        super(module, "watchlist");
        this.repository = this.getConfig().watcherRepository;
    }

    @Override
    public void runCommand(CommandEvent event) {
        this.sendWaiting(event, message -> {
            List<Watcher> watchers = repository.getFromUser(event.getAuthor().getIdLong());

            if (watchers.size() == 0) {
                this.sendError(message, "You don't have any watchers.");
            } else {
                new EmbedPage<Watcher>(message, watchers, 10) {
                    @Override
                    public EmbedBuilder getEmbed() {
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setTitle("Watchers : ");
                        return builder;
                    }
                };
            }
        });
    }
}
