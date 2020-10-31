package fr.alexpado.bots.cmb.interfaces.command;

import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.modules.crossout.models.FakeItem;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Item;
import fr.alexpado.bots.cmb.modules.crossout.repositories.FakeItemRepository;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public abstract class ItemBotCommand extends TranslatableBotCommand {

    public ItemBotCommand(JDAModule module, String label) {

        super(module, label);
    }

    @Override
    public List<String> getRequiredTranslation() {

        return Arrays.asList(Translation.GENERAL_ERROR, Translation.ITEMS_LIST, Translation.ITEMS_NOTFOUND, Translation.GENERAL_INVITE, Translation.XODB_OFFLINE, this
                .getDescription());
    }

    public void manageItemList(CommandEvent event, Message message, List<Item> items, String itemName) throws MissingTranslationException {

        if (items.isEmpty()) {
            if (itemName != null) {
                String query = "%" + itemName.toLowerCase() + "%";

                FakeItemRepository repository       = this.getConfig().getRepositoryAccessor().getFakeItemRepository();
                Optional<FakeItem> fakeItemOptional = repository.findEasterEgg(query);

                if (fakeItemOptional.isPresent()) {
                    message.editMessage(fakeItemOptional.get()
                                                        .getAsEmbed(event.getJDA(), this.getTranslation(Translation.GENERAL_INVITE))
                                                        .build()).queue();
                    return;
                }
            }
            this.sendError(message, this.getTranslation(Translation.ITEMS_NOTFOUND));
        } else if (items.size() == 1) {
            Item item = items.get(0);
            item.fetchTranslations(this.getEffectiveLanguage());
            message.editMessage(item.getAsEmbed(event.getJDA()).build()).queue();
        } else {
            // Try to find a perfect match.
            for (Item item : items) {
                if (item.getAvailableName().equalsIgnoreCase(itemName)) {
                    // Perfect match !
                    item.fetchTranslations(this.getEffectiveLanguage());
                    message.editMessage(item.getAsEmbed(event.getJDA()).build()).queue();
                    return;
                }
            }

            // Show the navigation menu.
            new EmbedPage<>(message, items, 20) {

                @Override
                public EmbedBuilder getEmbed() {

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(ItemBotCommand.this.getTranslation(Translation.ITEMS_LIST));
                    return builder;
                }
            };
        }
    }

}

