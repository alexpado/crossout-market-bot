package xo.marketbot.commands;

import org.springframework.stereotype.Component;
import xo.marketbot.XoMarketApplication;
import xo.marketbot.commands.meta.NoticeCommandMeta;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.services.commands.DiscordCommand;
import xo.marketbot.library.services.commands.annotations.Command;
import xo.marketbot.library.services.commands.annotations.Flags;
import xo.marketbot.library.services.commands.annotations.Param;
import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;

import java.util.List;

@Component
public class NoticeCommand extends DiscordCommand {

    /**
     * Creates a new {@link DiscordCommand} instance and register it immediately.
     *
     * @param discordBot
     *         The {@link IDiscordBot} associated with this command.
     */
    protected NoticeCommand(IDiscordBot discordBot) {

        super(discordBot);
    }

    /**
     * Retrieve the command meta for this {@link ICommand}.
     *
     * @return An {@link ICommandMeta} instance.
     */
    @Override
    public ICommandMeta getMeta() {

        return new NoticeCommandMeta();
    }

    @Command("notice...")
    public Object defineNotice(ICommandContext context, @Param("notice") String notice, @Flags List<String> flag) {

        if (!context.getUserEntity().getId().equals(149279150648066048L)) {
            return null;
        }

        if (flag.contains("r")) {
            XoMarketApplication.NOTICE = null;
        } else {
            XoMarketApplication.NOTICE = notice;
        }

        return null;
    }
}
