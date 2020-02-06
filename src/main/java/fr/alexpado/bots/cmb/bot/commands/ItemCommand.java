package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.interfaces.BotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.game.Item;
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
            this.sendError(message, this.getTranslation(Translation.ITEM_NOT_FOUND));
        } else if (items.size() == 1) {
            Item item = items.get(0);
            item.setTranslations(this.getTranslations());
            message.editMessage(item.getAsEmbed(event.getJDA()).build()).queue();
        } else {
            new EmbedPage<Item>(message, items, 20) {
                @Override
                public EmbedBuilder getEmbed() {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(ItemCommand.this.getTranslation(Translation.ITEM_LIST));
                    return builder;
                }
            };
        }
    }

    @Override
    public List<String> getLanguageKeys() {
        return Arrays.asList(Translation.ITEM_NOT_FOUND, Translation.ITEM_LIST, Translation.ITEM_REMOVED_LABEL, Translation.ITEM_REMOVED_DESC, Translation.DISCORD_INVITE, Translation.ITEM_BUY, Translation.ITEM_SELL, Translation.ITEM_CRAFT_BUY, Translation.ITEM_CRAFT_SELL, Translation.CURRENCY);
    }

}
