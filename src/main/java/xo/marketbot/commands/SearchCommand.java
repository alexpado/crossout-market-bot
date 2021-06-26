package xo.marketbot.commands;

import org.springframework.stereotype.Component;
import xo.marketbot.commands.meta.SearchCommandMeta;
import xo.marketbot.entities.interfaces.game.ICategory;
import xo.marketbot.entities.interfaces.game.IFaction;
import xo.marketbot.entities.interfaces.game.IItem;
import xo.marketbot.entities.interfaces.game.IRarity;
import xo.marketbot.i18n.messages.categories.CategoryListEmbed;
import xo.marketbot.i18n.messages.factions.FactionListEmbed;
import xo.marketbot.i18n.messages.items.ItemListEmbed;
import xo.marketbot.i18n.messages.items.NoItemFoundEmbed;
import xo.marketbot.i18n.messages.rartities.RarityListEmbed;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.services.commands.DiscordCommand;
import xo.marketbot.library.services.commands.annotations.Args;
import xo.marketbot.library.services.commands.annotations.Command;
import xo.marketbot.library.services.commands.annotations.Flags;
import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;
import xo.marketbot.xodb.XoDB;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchCommand extends DiscordCommand {

    private final XoDB db;

    /**
     * Creates a new {@link DiscordCommand} instance and register it immediately.
     *
     * @param discordBot
     *         The {@link IDiscordBot} associated with this command.
     */
    protected SearchCommand(IDiscordBot discordBot, XoDB db) {

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

        return new SearchCommandMeta();
    }

    @Command("query...")
    public Object searchItems(ICommandContext context, @Flags List<String> flags, @Args Map<String, String> arguments) throws Exception {

        HashMap<String, Object> search = new HashMap<>();

        if (arguments.containsKey("faction")) {

            String         name  = arguments.get("faction");
            List<IFaction> items = this.db.factions().findAll().complete();

            if (items.stream().anyMatch(item -> item.getName().equalsIgnoreCase(name))) {
                // It's a valid name !
                search.put("faction", name);
            } else {
                return new FactionListEmbed(items);
            }
        }

        if (arguments.containsKey("category")) {

            String          name  = arguments.get("category");
            List<ICategory> items = this.db.categories().findAll().complete();

            if (items.stream().anyMatch(item -> item.getName().equalsIgnoreCase(name))) {
                // It's a valid name !
                search.put("category", name);
            } else {
                return new CategoryListEmbed(items);
            }
        }

        if (arguments.containsKey("rarity")) {

            String        name  = arguments.get("rarity");
            List<IRarity> items = this.db.rarities().findAll().complete();

            if (items.stream().anyMatch(item -> item.getName().equalsIgnoreCase(name))) {
                // It's a valid name !
                search.put("rarity", name);
            } else {
                return new RarityListEmbed(items);
            }
        }

        search.put("removedItems", flags.contains("r") || flags.contains("removedItems"));
        search.put("metaItems", flags.contains("m") || flags.contains("metaItems"));
        search.put("language", context.getChannelEntity().getEffectiveLanguage());

        List<IItem> items = this.db.items().findAll(search).complete();
        if (items.isEmpty()) {
            return new NoItemFoundEmbed();
        } else if (items.size() == 1) {
            return items.get(0);
        } else {
            return new ItemListEmbed(items);
        }
    }


}
