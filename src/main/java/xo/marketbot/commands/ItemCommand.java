package xo.marketbot.commands;

import org.springframework.stereotype.Component;
import xo.marketbot.commands.meta.ItemCommandMeta;
import xo.marketbot.entities.interfaces.game.IItem;
import xo.marketbot.i18n.messages.items.ItemListEmbed;
import xo.marketbot.i18n.messages.items.NoItemFoundEmbed;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.services.commands.DiscordCommand;
import xo.marketbot.library.services.commands.annotations.Command;
import xo.marketbot.library.services.commands.annotations.Flags;
import xo.marketbot.library.services.commands.annotations.Param;
import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;
import xo.marketbot.xodb.XoDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@Component
public class ItemCommand extends DiscordCommand {

    private final XoDB db;

    protected ItemCommand(IDiscordBot discordBot, XoDB xoDB) {

        super(discordBot);
        this.db = xoDB;
    }

    /**
     * Retrieve the command meta for this {@link ICommand}.
     *
     * @return An {@link ICommandMeta} instance.
     */
    @Override
    public ICommandMeta getMeta() {

        return new ItemCommandMeta();
    }

    @Command("name...")
    public Object viewItemEmbed(ICommandContext context, @Param("name") String itemName, @Flags List<String> flags) throws Exception {

        Map<String, Object> search = new HashMap<>();

        if (flags.contains("r")) { // Are we including removed items ?
            search.put("removedItems", "true");
        }

        if (flags.contains("m")) { // Are we including meta items ?
            search.put("metaItems", "true");
        }

        search.put("query", itemName);
        search.put("language", context.getChannelEntity().getEffectiveLanguage());

        List<IItem> items = this.db.items().findAll(search).complete();

        if (items.isEmpty()) {
            return new NoItemFoundEmbed();
        } else if (items.size() == 1) {
            return items.get(0);
        } else {
            Optional<IItem> optionalItem = items.stream().filter(i -> i.getName().equalsIgnoreCase(itemName)).findFirst();

            if (optionalItem.isPresent()) {
                return optionalItem.get();
            }

            return new ItemListEmbed(items);
        }

    }

}
