package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.enums.WatcherType;
import fr.alexpado.bots.cmb.interfaces.command.WatcherCommandGroup;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.Watcher;
import fr.alexpado.bots.cmb.models.game.Item;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpBuilder;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpSection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WatchCommand extends WatcherCommandGroup {

    public WatchCommand(JDAModule module) {
        super(module, "watch");
    }

    @Override
    public List<String> getRequiredTranslation() {
        List<String> requiredTranslations = new ArrayList<>(super.getRequiredTranslation());
        requiredTranslations.addAll(Arrays.asList(
                Translation.WATCHERS_WRONG_FOR,
                Translation.ITEMS_NOTFOUND,
                Translation.ITEMS_MULTIPLE,
                Translation.WATCHERS_NEW
        ));
        return requiredTranslations;
    }


    @Override
    public void execute(CommandEvent event, Message message) {
        Optional<String> optionalItemName = this.getItemName(event);

        if (!optionalItemName.isPresent()) {
            this.sendError(message, this.getTranslation(Translation.WATCHERS_WRONG_FOR));
            return;
        }

        ItemEndpoint itemEndpoint = new ItemEndpoint(this.getConfig());
        List<Item> items = itemEndpoint.searchByName(optionalItemName.get(), this.getDiscordUser().getLanguage());

        Item item = null;

        if (items.size() == 0) {
            this.sendError(message, this.getTranslation(Translation.ITEMS_NOTFOUND));
            return;
        } else if (items.size() > 1) {
            // Try to find a perfect match.
            for (Item el : items) {
                if (el.getName().equalsIgnoreCase(optionalItemName.get())) {
                    // Perfect match !
                    item = el;
                    break;
                }
            }

            if (item == null) {
                this.sendError(message, this.getTranslation(Translation.ITEMS_MULTIPLE));
                return;
            }
        } else {
            item = items.get(0);
        }

        Watcher watcher = Watcher.createFor(this.getDiscordUser(), item);

        Optional<WatcherType> optionalWatcherType = this.getType(event);
        Optional<Float> optionalPrice = this.getPrice(event);
        Optional<Long> optionalInterval = this.getTime(event);

        if (optionalWatcherType.isPresent()) {
            WatcherType watcherType = optionalWatcherType.get();

            if (watcherType == WatcherType.UNKNOWN) {
                this.sendError(message, Translation.WATCHERS_WRONG_TYPE);
                return;
            }
            watcher.setWatcherType(watcherType.getId());

            if (!optionalPrice.isPresent()) {
                this.sendError(message, Translation.WATCHERS_WRONG_PRICE);
                return;
            }
            Float price = optionalPrice.get();
            watcher.setPrice(price);
        } else {
            watcher.setWatcherType(WatcherType.NORMAL.getId());
        }

        optionalInterval.ifPresent(watcher::setRepeatEvery);

        this.getRepository().save(watcher);
        this.sendInfo(message, this.getTranslation(Translation.WATCHERS_NEW));
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {
        EmbedBuilder builder = super.getAdvancedHelp();
        builder.setTitle("Advanced help for : watch");

        AdvancedHelpBuilder helpBuilder = new AdvancedHelpBuilder();

        helpBuilder.setDescription(this.getTranslation(this.getDescription()));

        AdvancedHelpSection parametersSection = new AdvancedHelpSection("Usage");
        parametersSection.addField("**`cm:watch <item name>`**", "Add a watcher on the provided item with default config.");
        parametersSection.addField("**`cm:watch when <buy|sell> (price) <over|under> <price> for <item name> `**", "Add a price watcher on the provided item. (`(price)` is an optional word)");
        parametersSection.addField("**`cm:watch every <interval> for <item name> `**", "Add a watcher on the provided item with custom interval.");
        parametersSection.addField("**`cm:watch when <buy|sell> (price) <over|under> <price> every <interval> for <item name> `**", "Add a price watcher on the provided item with custom interval.");

        AdvancedHelpSection examplesSection = new AdvancedHelpSection("Example");
        examplesSection.addField("`cm:watch Growl`", "Add a watcher on the `Growl` item.");
        examplesSection.addField("`cm:watch when sell price over 45.10 for Growl`", "Add a watcher on the `Growl` item and send the message only if the you can sell it for more than 45.10 Coins.");
        examplesSection.addField("`cm:watch when buy price under 45.10 every 4h35m for Growl`", "Add a watcher on the `Growl` item that will be executed every 4 hours and 35 minutes and send the message only if the you can buy it for less than 45.10 Coins.");

        helpBuilder.addSection(parametersSection);
        helpBuilder.addSection(examplesSection);

        builder.setDescription(helpBuilder.toString());
        return builder;
    }
}
