package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.interfaces.command.TranslatableBotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.FakeItem;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.game.Item;
import fr.alexpado.bots.cmb.repositories.FakeItemRepository;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.*;

public class ItemCommand extends TranslatableBotCommand {

    public ItemCommand(JDAModule module) {
        super(module, "item");
    }

    @Override
    public void execute(CommandEvent event, Message message) throws MissingTranslationException {
        ItemEndpoint endpoint = new ItemEndpoint(this.getConfig());
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
        params.put("language", this.getEffectiveLanguage());
        List<Item> items = endpoint.search(params);

        if (items.size() == 0) {

            String query = "%" + itemName.toLowerCase() + "%";

            FakeItemRepository repository = this.getConfig().getFakeItemRepository();
            Optional<FakeItem> fakeItemOptional = repository.findEasterEgg(query);

            if (fakeItemOptional.isPresent()) {
                message.editMessage(fakeItemOptional.get().getAsEmbed(event.getJDA(), this.getTranslation(Translation.GENERAL_INVITE)).build()).queue();
            } else {
                this.sendError(message, this.getTranslation(Translation.ITEMS_NOTFOUND));
            }
        } else if (items.size() == 1) {
            Item item = items.get(0);
            item.fetchTranslations(this.getEffectiveLanguage());
            message.editMessage(item.getAsEmbed(event.getJDA()).build()).queue();
        } else {

            // Try to find a perfect match.
            for (Item item : items) {
                if (item.getName().equalsIgnoreCase(itemName)) {
                    // Perfect match !
                    item.fetchTranslations(this.getEffectiveLanguage());
                    message.editMessage(item.getAsEmbed(event.getJDA()).build()).queue();
                    return;
                }
            }

            // Show the navigation menu.
            new EmbedPage<Item>(message, items, 20) {
                @Override
                public EmbedBuilder getEmbed() {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(ItemCommand.this.getTranslation(Translation.ITEMS_LIST));
                    return builder;
                }
            };
        }
    }

    @Override
    public List<String> getRequiredTranslation() {
        return Arrays.asList(
                Translation.ITEMS_NOTFOUND,
                Translation.ITEMS_LIST,
                Translation.ITEMS_UNAVAILABLE,
                Translation.GENERAL_INVITE
        );
    }

}
