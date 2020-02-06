package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.interfaces.BotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.repositories.DiscordUserRepository;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;

public class PauseWatchersCommand extends BotCommand {

    public PauseWatchersCommand(JDAModule module) {
        super(module, "pausewatchers");
    }

    @Override
    public List<String> getLanguageKeys() {
        return Arrays.asList(Translation.WATCHERS_PAUSED, Translation.WATCHERS_RESUMED);
    }

    @Override
    public void execute(CommandEvent event, Message message) {

        DiscordUserRepository dur = this.getBot().getConfig().discordUserRepository;
        DiscordUser user = this.getDiscordUser(event);

        String key;

        if (user.isWatcherPaused()) {
            user.setWatcherPaused(false);
            key = Translation.WATCHERS_RESUMED;
        } else {
            user.setWatcherPaused(true);
            key = Translation.WATCHERS_PAUSED;
        }

        dur.save(user);
        this.sendInfo(message, this.getTranslation(key));
    }
}
