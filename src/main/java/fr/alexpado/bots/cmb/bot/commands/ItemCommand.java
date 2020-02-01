package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.models.game.Item;
import fr.alexpado.bots.cmb.bot.BotCommand;
import fr.alexpado.bots.cmb.libs.TKey;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCommand extends BotCommand {

    public ItemCommand(JDAModule module) {
        super(module, "item");
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        ItemEndpoint endpoint = new ItemEndpoint(this.getConfig().getApiHost());
        Map<String, String> params = new HashMap<>();

        if (event.getArgs().contains("-r")) {
            params.put("removedItems", "true");
            event.getArgs().remove("-r");
        }

        if (event.getArgs().contains("-m")) {
            params.put("metaItems", "true");
            event.getArgs().remove("-m");
        }

        String itemName = String.join(" ", event.getArgs());
        params.put("query", itemName);
        List<Item> items = endpoint.search(params);

        if (items.size() == 0) {
            this.sendError(message, this.getTranslation(TKey.ITEM_NOT_FOUND));
        } else if (items.size() == 1) {
            Item item = items.get(0);
            item.setTranslations(this.getTranslations());
            message.editMessage(item.getAsEmbed(event.getJDA()).build()).queue();
        } else {
            new EmbedPage<Item>(message, items, 20) {
                @Override
                public EmbedBuilder getEmbed() {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(ItemCommand.this.getTranslation(TKey.ITEM_LIST));
                    return builder;
                }
            };
        }
    }

    @Override
    public List<String> getLanguageKeys() {
        return Arrays.asList(TKey.ITEM_NOT_FOUND, TKey.ITEM_LIST, TKey.ITEM_REMOVED_LABEL, TKey.ITEM_REMOVED_DESC, TKey.DISCORD_INVITE, TKey.ITEM_BUY, TKey.ITEM_SELL, TKey.ITEM_CRAFT_BUY, TKey.ITEM_CRAFT_SELL, TKey.CURRENCY);
    }

}
