package xo.marketbot.commands;

import org.springframework.stereotype.Component;
import xo.marketbot.commands.meta.PackCommandMeta;
import xo.marketbot.entities.interfaces.game.IPack;
import xo.marketbot.i18n.messages.packs.NoPackFoundEmbed;
import xo.marketbot.i18n.messages.packs.PackListEmbed;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.services.commands.DiscordCommand;
import xo.marketbot.library.services.commands.annotations.Command;
import xo.marketbot.library.services.commands.annotations.Param;
import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;
import xo.marketbot.xodb.XoDB;

import java.util.ArrayList;
import java.util.List;

@Component
public class PackCommand extends DiscordCommand {

    private final XoDB db;

    protected PackCommand(IDiscordBot discordBot, XoDB db) {

        super(discordBot);
        this.db = db;
    }

    /**
     * Retrieve the command meta for this {@link ICommand}.
     *
     * @return An {@link ICommandMeta} instance.
     */
    @Override
    public ICommandMeta getMeta() {

        return new PackCommandMeta();
    }

    @Command("name...")
    public Object viewPackEmbed(ICommandContext context, @Param("name") String packName) throws Exception {

        // Perhaps monitor when pack search will be available to implement.
        List<IPack> packs     = this.db.packs().findAll().complete();
        List<IPack> effective = new ArrayList<>();

        for (IPack pack : packs) {

            if (pack.getName().equalsIgnoreCase(packName)) {
                // Perfect match !
                return pack.toEmbed(context.getEvent().getJDA());
            }

            if (pack.getName().toLowerCase().contains(packName.toLowerCase())) {
                effective.add(pack);
            }

        }

        if (effective.isEmpty()) {
            return new NoPackFoundEmbed();
        } else if (effective.size() == 1) {
            return effective.get(0);
        } else {
            return new PackListEmbed(effective);
        }
    }
}
