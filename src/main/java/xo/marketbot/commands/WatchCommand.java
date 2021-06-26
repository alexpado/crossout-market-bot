package xo.marketbot.commands;

import org.springframework.stereotype.Component;
import xo.marketbot.commands.meta.WatchCommandMeta;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.entities.interfaces.game.IItem;
import xo.marketbot.i18n.messages.items.NoItemFoundEmbed;
import xo.marketbot.i18n.messages.items.TooManyItemEmbed;
import xo.marketbot.i18n.messages.watchers.WrongForEmbed;
import xo.marketbot.library.interfaces.IDiscordBot;
import xo.marketbot.library.services.commands.DiscordCommand;
import xo.marketbot.library.services.commands.annotations.Command;
import xo.marketbot.library.services.commands.annotations.Param;
import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandContext;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;
import xo.marketbot.repositories.WatcherRepository;
import xo.marketbot.xodb.XoDB;

import java.util.*;

@Component
public class WatchCommand extends DiscordCommand {

    private final XoDB              db;
    private final WatcherRepository repository;

    /**
     * Creates a new {@link DiscordCommand} instance and register it immediately.
     *
     * @param discordBot
     *         The {@link IDiscordBot} associated with this command.
     * @param db
     *         The {@link XoDB} instance allowing to access CrossoutDB API
     * @param repository
     *         The {@link WatcherRepository} allowing to interact with {@link Watcher} entities
     */
    protected WatchCommand(IDiscordBot discordBot, XoDB db, WatcherRepository repository) {

        super(discordBot);
        this.db         = db;
        this.repository = repository;
    }

    /**
     * Retrieve the command meta for this {@link ICommand}.
     *
     * @return An {@link ICommandMeta} instance.
     */
    @Override
    public ICommandMeta getMeta() {

        return new WatchCommandMeta();
    }

    @Command("content...") // Retro-compatibility (shameful copy/paste) ─ Subject to changes, the code is way to messy
    public Object setWatcher(ICommandContext context, @Param("content") String content) throws Exception {

        List<String> args = Arrays.asList(content.split(" "));

        int whenIndex  = args.indexOf("when");
        int everyIndex = args.indexOf("every");
        int forIndex   = args.indexOf("for");
        int priceIndex = args.indexOf("price");

        String itemName;

        if (((everyIndex != -1 || whenIndex != -1) && forIndex == -1) || forIndex < everyIndex || forIndex < whenIndex) {
            return new WrongForEmbed();
        }

        if (forIndex != -1) {
            itemName = String.join(" ", args.subList(forIndex + 1, args.size()));
        } else {
            itemName = content;
        }

        Map<String, Object> search = new HashMap<>();
        search.put("query", itemName);
        search.put("language", context.getChannelEntity().getEffectiveLanguage());

        List<IItem> items = this.db.items().findAll(search).complete();
        IItem       item;

        if (items.isEmpty()) {
            return new NoItemFoundEmbed();
        } else if (items.size() == 1) {
            item = items.get(0);
        } else {
            Optional<IItem> optionalItem = items.stream().filter(i -> i.getName().equalsIgnoreCase(itemName)).findFirst();

            if (optionalItem.isPresent()) {
                item = optionalItem.get();
            }

            return new TooManyItemEmbed();
        }
        // TODO: Create watcher
        return item.toEmbed(context.getEvent().getJDA());
    }

}
