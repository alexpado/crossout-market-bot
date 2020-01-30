package fr.alexpado.bots.cmb.discord.commands;

import fr.alexpado.bots.cmb.crossout.api.ItemEndpoint;
import fr.alexpado.bots.cmb.crossout.models.Item;
import fr.alexpado.bots.cmb.discord.BotCommand;
import fr.alexpado.bots.cmb.libs.jdamodules.JDAModule;
import fr.alexpado.bots.cmb.libs.jdamodules.events.CommandEvent;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCommand extends BotCommand {

    public ItemCommand(JDAModule module) {
        super(module, "item");
    }

    @Override
    public void runCommand(CommandEvent event) {
        this.sendWaiting(event, message -> {
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
                this.sendError(message, "No item found !");
            } else if (items.size() == 1) {
                message.editMessage(items.get(0).getAsEmbed(event.getJDA()).build()).queue();
            } else {
                new EmbedPage<Item>(message, items, 20) {
                    @Override
                    public EmbedBuilder getEmbed() {
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setTitle("List of item :");
                        return builder;
                    }
                };
            }
        });
    }

}
