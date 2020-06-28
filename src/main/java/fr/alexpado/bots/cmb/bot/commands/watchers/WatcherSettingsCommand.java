package fr.alexpado.bots.cmb.bot.commands.watchers;

import fr.alexpado.bots.cmb.api.ItemEndpoint;
import fr.alexpado.bots.cmb.enums.WatcherType;
import fr.alexpado.bots.cmb.interfaces.command.WatcherCommandGroup;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.modules.crossout.models.Watcher;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Item;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpBuilder;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpSection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class WatcherSettingsCommand extends WatcherCommandGroup {

    public WatcherSettingsCommand(JDAModule module) {

        super(module, "watchersettings");
    }

    @Override
    public List<String> getRequiredTranslation() {

        List<String> requiredTranslations = new ArrayList<>(super.getRequiredTranslation());
        requiredTranslations.addAll(Arrays.asList(Translation.WATCHERS_WRONG_ID, Translation.GENERAL_ERROR, Translation.WATCHERS_REMOVED, Translation.WATCHERS_UPDATED));
        return requiredTranslations;
    }

    @Override
    public void execute(CommandEvent event, Message message) {

        Optional<Watcher> optionalWatcher = this.getWatcher(message, event);
        if (!optionalWatcher.isPresent()) {
            return;
        }
        Watcher watcher = optionalWatcher.get();

        ItemEndpoint   endpoint     = new ItemEndpoint(this.getConfig());
        Optional<Item> optionalItem = endpoint.getOne(watcher.getItemId());

        if (!optionalItem.isPresent()) {
            this.sendError(message, this.getTranslation(Translation.GENERAL_ERROR));
            return;
        }
        Item item = optionalItem.get();

        if (item.isRemoved()) {
            this.sendWarn(message, this.getTranslation(Translation.WATCHERS_REMOVED));
            return;
        }

        Optional<WatcherType> optionalWatcherType = this.getType(event);
        Optional<Float>       optionalPrice       = this.getPrice(event);
        Optional<Long>        optionalInterval    = this.getTime(event);

        if (optionalWatcherType.isPresent()) {
            WatcherType watcherType = optionalWatcherType.get();

            if (watcherType == WatcherType.NORMAL) {
                watcher.setWatcherType(watcherType.getId());
            } else if (!optionalPrice.isPresent()) {
                this.sendError(message, this.getTranslation(Translation.WATCHERS_WRONG_PRICE));
                return;
            } else {
                Float price = optionalPrice.get();
                watcher.setWatcherType(watcherType.getId());
                watcher.setPrice(price);
            }
        }

        optionalInterval.ifPresent(watcher::setRepeatEvery);

        this.getRepository().save(watcher);
        this.sendInfo(message, this.getTranslation(Translation.WATCHERS_UPDATED));
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {

        EmbedBuilder builder = super.getAdvancedHelp();
        builder.setTitle("Advanced help for : watchersettings");

        AdvancedHelpBuilder helpBuilder = new AdvancedHelpBuilder();

        helpBuilder.setDescription(this.getTranslation(this.getDescription()));

        AdvancedHelpSection parametersSection = new AdvancedHelpSection("Usage");
        parametersSection.addField("**`cm:watchersettings <id> when <buy|sell> (price) <over|under> <price>`**", "Change your price watcher settings.");
        parametersSection.addField("**`cm:watchersettings <id> every <interval>`**", "Change your watcher interval.");
        parametersSection.addField("**`cm:watchersettings <id> always`**", "Change your price watcher to a normal watcher.");

        AdvancedHelpSection examplesSection = new AdvancedHelpSection("Example");
        examplesSection.addField("`cm:watchersettings 404 when sell price over 66.66`", "Change the watcher with ID 404 to a price watcher.");
        examplesSection.addField("`cm:watchersettings 404 always`", "Change the watcher with ID 404 to a normal watcher.");
        examplesSection.addField("`cm:watchersettings 404 every 1h`", "Change the watcher with ID 404 interval.");

        helpBuilder.addSection(parametersSection);
        helpBuilder.addSection(examplesSection);

        builder.setDescription(helpBuilder.toString());
        return builder;
    }

}
