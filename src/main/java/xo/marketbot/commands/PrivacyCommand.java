package xo.marketbot.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import xo.marketbot.commands.meta.PrivacyCommandMeta;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.services.commands.DiscordCommand;
import xo.marketbot.library.services.commands.annotations.Command;
import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;
import xo.marketbot.library.services.translations.annotations.I18N;
import xo.marketbot.tools.DiscordEmbed;

@Component
public class PrivacyCommand extends DiscordCommand {

    /**
     * Creates a new {@link DiscordCommand} instance and register it immediately.
     *
     * @param discordBot
     *         The {@link IDiscordBot} associated with this command.
     */
    protected PrivacyCommand(IDiscordBot discordBot) {

        super(discordBot);
    }

    /**
     * Retrieve the command meta for this {@link ICommand}.
     *
     * @return An {@link ICommandMeta} instance.
     */
    @Override
    public ICommandMeta getMeta() {

        return new PrivacyCommandMeta();
    }

    @Command("match...")
    public Object show(ICommandContext context) {

        return new DiscordEmbed(context.getEvent().getJDA()) {

            @I18N("general.privacy")
            private String content;

            @NotNull
            @Override
            public MessageEmbed build() {

                this.setMarkup(this.content);
                return super.build();
            }
        };

    }
}
