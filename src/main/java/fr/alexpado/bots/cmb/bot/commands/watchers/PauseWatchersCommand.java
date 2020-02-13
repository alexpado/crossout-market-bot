package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.interfaces.command.WatcherCommandGroup;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
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
        requiredTranslations.addAll(Arrays.asList(
                Translation.WATCHERS_PAUSED,
                Translation.WATCHERS_RESUMED
        ));
        return requiredTranslations;
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        DiscordUser user = this.getDiscordUser();
        String key;

        if (user.isWatcherPaused()) {
            user.setWatcherPaused(false);
            key = Translation.WATCHERS_RESUMED;
        } else {
            user.setWatcherPaused(true);
            key = Translation.WATCHERS_PAUSED;
        }

        this.getConfig().getDiscordUserRepository().save(user);
        this.sendInfo(message, this.getTranslation(key));
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {
        return null;
    }
}
