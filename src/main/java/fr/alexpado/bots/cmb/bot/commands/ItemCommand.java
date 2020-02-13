package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.interfaces.command.ItemBotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpBuilder;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpSection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemCommand extends ItemBotCommand {

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

        this.manageItemList(event, message, endpoint.search(params), itemName);
    }

    @Override
    public List<String> getRequiredTranslation() {
        return super.getRequiredTranslation();
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {
        EmbedBuilder builder = super.getAdvancedHelp();
        builder.setTitle("Advanced help for : item");

        AdvancedHelpBuilder helpBuilder = new AdvancedHelpBuilder();

        helpBuilder.setDescription(this.getTranslation(this.getDescription()));

        AdvancedHelpSection parameters = new AdvancedHelpSection("Parameters");
        parameters.addField("`-r`", "Include removed items in the result list.");
        parameters.addField("`-m`", "Include meta items in the result list.");

        AdvancedHelpSection usages = new AdvancedHelpSection("Usage");
        usages.addField("**`cm:item <item name>`**", "Search for an item. Show the detail if only one is found.");

        AdvancedHelpSection examples = new AdvancedHelpSection("Example");
        examples.addField("`cm:item gr`", "Show all item with `gr` in their name.");
        examples.addField("`cm:item gr -m -r`", "Show all item with `gr` in their name, including removed items and meta items.");

        helpBuilder.addSection(parameters);
        helpBuilder.addSection(usages);
        helpBuilder.addSection(examples);

        builder.setDescription(helpBuilder.toString());
        return builder;
    }

}
