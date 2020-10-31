package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.interfaces.command.WatcherCommandGroup;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.modules.crossout.models.Watcher;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpBuilder;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpSection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class UnwatchCommand extends WatcherCommandGroup {

    public UnwatchCommand(JDAModule module) {

        super(module, "unwatch");
    }

    @Override
    public List<String> getRequiredTranslation() {

        List<String> requiredTranslations = new ArrayList<>(super.getRequiredTranslation());
        requiredTranslations.addAll(Arrays.asList(Translation.WATCHERS_WRONG_ID, Translation.WATCHERS_UNWATCH, this.getDescription()));
        return requiredTranslations;
    }

    @Override
    public void execute(CommandEvent event, Message message) {

        Optional<Watcher> optionalWatcher = this.getWatcher(message, event);

        if (optionalWatcher.isPresent()) {
            this.getRepository().delete(optionalWatcher.get());
            this.sendInfo(message, this.getTranslation(Translation.WATCHERS_UNWATCH));
        }
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {

        EmbedBuilder builder = super.getAdvancedHelp();
        builder.setTitle("Advanced help for : unwatch");

        AdvancedHelpBuilder helpBuilder = new AdvancedHelpBuilder();

        String desc = this.getTranslation(this.getDescription()) + "\nTo retrieve the ID of one of your watchers, use `cm:watchlist`";
        helpBuilder.setDescription(desc);

        AdvancedHelpSection parametersSection = new AdvancedHelpSection("Usage");
        parametersSection.addField("**`cm:unwatch <id>`**", "Remove your watcher with the ID provided");

        AdvancedHelpSection examplesSection = new AdvancedHelpSection("Example");
        examplesSection.addField("`cm:unwatch 404`", "Remove your watcher with ID 404.");

        helpBuilder.addSection(parametersSection);
        helpBuilder.addSection(examplesSection);

        builder.setDescription(helpBuilder.toString());
        return builder;
    }

}
