package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.interfaces.command.ItemBotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
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

}
