package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.api.CategoryEndpoint;
import fr.alexpado.bots.cmb.api.FactionEndpoint;
import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.api.RarityEndpoint;
import fr.alexpado.bots.cmb.interfaces.command.ItemBotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.modules.crossout.models.OldTranslation;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Category;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Faction;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Item;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Rarity;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import fr.alexpado.bots.cmb.tools.CommandArgumentsParser;
import fr.alexpado.bots.cmb.tools.Utilities;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpBuilder;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpSection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Message;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class SearchCommand extends ItemBotCommand {

    public SearchCommand(JDAModule module) {

        super(module, "search");
    }

    @Override
    public List<String> getRequiredTranslation() {

        return Utilities.mergeList(super.getRequiredTranslation(), OldTranslation.RARITIES_INVALID, OldTranslation.CATEGORIES_INVALID, OldTranslation.FACTIONS_INVALID, OldTranslation.TYPES_INVALID);
    }

    @Override
    public void execute(CommandEvent event, Message message) throws MissingTranslationException {


        if (event.getJDA().getPresence().getStatus() == OnlineStatus.DO_NOT_DISTURB) {
            this.sendError(message, this.getTranslation(OldTranslation.XODB_OFFLINE));
            return;
        }

        CommandArgumentsParser parser = new CommandArgumentsParser(event);

        boolean removedItems = parser.get("removedItems").isPresent();
        boolean metaItems    = parser.get("metaItems").isPresent();

        HashMap<String, String> query = new HashMap<>();

        Optional<String> optionalRarity   = parser.get("rarity");
        Optional<String> optionalCategory = parser.get("category");
        Optional<String> optionalFaction  = parser.get("faction");

        if (optionalRarity.isPresent()) {
            String rarityName = optionalRarity.get();

            RarityEndpoint rarityEndpoint = new RarityEndpoint(this.getConfig().getApiHost());
            List<Rarity>   rarities       = rarityEndpoint.getAll();
            boolean isValid = rarities.stream().anyMatch(rarity -> rarity.getName().equalsIgnoreCase(rarityName));

            if (isValid) {
                query.put("rarity", rarityName);
            } else {
                new EmbedPage<Rarity>(message, rarities, 10) {

                    @Override
                    public EmbedBuilder getEmbed() {

                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setColor(Color.ORANGE);
                        builder.setTitle(SearchCommand.this.getTranslation(OldTranslation.RARITIES_INVALID));
                        return builder;
                    }
                };
                return;
            }
        }

        if (optionalCategory.isPresent()) {
            String categoryName = optionalCategory.get();

            CategoryEndpoint categoryEndpoint = new CategoryEndpoint(this.getConfig().getApiHost());
            List<Category>   categories       = categoryEndpoint.getAll();
            boolean isValid = categories.stream()
                                        .anyMatch(category -> category.getName().equalsIgnoreCase(categoryName));

            if (isValid) {
                query.put("category", categoryName);
            } else {
                new EmbedPage<Category>(message, categories, 10) {

                    @Override
                    public EmbedBuilder getEmbed() {

                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setColor(Color.ORANGE);
                        builder.setTitle(SearchCommand.this.getTranslation(OldTranslation.CATEGORIES_INVALID));
                        return builder;
                    }
                };
                return;
            }
        }

        if (optionalFaction.isPresent()) {
            String factionName = optionalFaction.get();

            FactionEndpoint factionEndpoint = new FactionEndpoint(this.getConfig().getApiHost());
            List<Faction>   factions        = factionEndpoint.getAll();
            boolean isValid = factions.stream().anyMatch(faction -> faction.getName().equalsIgnoreCase(factionName));

            if (isValid) {
                query.put("faction", factionName);
            } else {
                new EmbedPage<Faction>(message, factions, 10) {

                    @Override
                    public EmbedBuilder getEmbed() {

                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setColor(Color.ORANGE);
                        builder.setTitle(SearchCommand.this.getTranslation(OldTranslation.FACTIONS_INVALID));
                        return builder;
                    }
                };
                return;
            }
        }

        if (removedItems) {
            query.put("removedItems", "true");
        }

        if (metaItems) {
            query.put("metaItems", "true");
        }

        query.put("language", this.getEffectiveLanguage());

        ItemEndpoint endpoint = new ItemEndpoint(this.getConfig());
        List<Item>   items    = endpoint.search(query);

        this.manageItemList(event, message, items, null);
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {

        EmbedBuilder builder = super.getAdvancedHelp();
        builder.setTitle("Advanced help for : search");

        AdvancedHelpBuilder helpBuilder = new AdvancedHelpBuilder();

        String desc = this.getTranslation(this.getDescription()) + "\nTo search for an item by its name, use `cm:item <item name>`";
        helpBuilder.setDescription(desc);

        AdvancedHelpSection parametersSection = new AdvancedHelpSection("Parameters");
        parametersSection.addField("**`--rarity <name>`**", "Find all items within this rarity.");
        parametersSection.addField("**`--category <name>`**", "Find all items within this category.");
        parametersSection.addField("**`--faction <name>`**", "Find all items within this faction.");

        AdvancedHelpSection examplesSection = new AdvancedHelpSection("Example");
        examplesSection.addField("`cm:search --rarity Common --faction Lunatics`", "Find all **Common** item within the **Lunatics** faction.");
        examplesSection.addField("`cm:search --rarity azerty`", "As the **azerty** rarity doesn't exists, a list of valid rarities will be shown. This work for every search parameters.");
        examplesSection.addField("**`--faction <name>`**", "Find all items within this faction.");

        helpBuilder.addSection(parametersSection);
        helpBuilder.addSection(examplesSection);


        builder.setDescription(helpBuilder.toString());
        return builder;
    }

}
