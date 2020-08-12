package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.interfaces.command.WatcherCommandGroup;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.modules.crossout.models.OldTranslation;
import fr.alexpado.bots.cmb.modules.crossout.models.settings.UserSettings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PauseWatchersCommand extends WatcherCommandGroup {

    public PauseWatchersCommand(JDAModule module) {

        super(module, "pausewatchers");
    }

    @Override
    public List<String> getRequiredTranslation() {

        List<String> requiredTranslations = new ArrayList<>(super.getRequiredTranslation());
        requiredTranslations.addAll(Arrays.asList(OldTranslation.WATCHERS_PAUSED, OldTranslation.WATCHERS_RESUMED));
        return requiredTranslations;
    }

    @Override
    public void execute(CommandEvent event, Message message) {

        UserSettings settings = this.getUserSettings();
        String       key;

        if (settings.isWatcherPaused()) {
            settings.setWatcherPaused(false);
            key = OldTranslation.WATCHERS_RESUMED;
        } else {
            settings.setWatcherPaused(true);
            key = OldTranslation.WATCHERS_PAUSED;
        }

        this.getUserSettingsRepository().save(settings);
        this.sendInfo(message, this.getTranslation(key));
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {

        return null;
    }

}
