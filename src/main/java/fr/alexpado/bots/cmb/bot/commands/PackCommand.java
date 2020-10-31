package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.api.PackEnpoint;
import fr.alexpado.bots.cmb.interfaces.command.TranslatableBotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Pack;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpBuilder;
import fr.alexpado.bots.cmb.tools.section.AdvancedHelpSection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackCommand extends TranslatableBotCommand {

    public PackCommand(JDAModule module) {

        super(module, "pack");
    }

    @Override
    public List<String> getRequiredTranslation() {

        return Arrays.asList(Translation.PACKS_NOTFOUND, Translation.PACKS_LIST, this.getDescription());
    }

    @Override
    public void execute(CommandEvent event, Message message) throws MissingTranslationException {

        if (event.getJDA().getPresence().getStatus() == OnlineStatus.DO_NOT_DISTURB) {
            this.sendError(message, this.getTranslation(Translation.XODB_OFFLINE));
            return;
        }

        PackEnpoint endpoint       = new PackEnpoint(this.getConfig());
        List<Pack>  packs          = endpoint.getAll();
        List<Pack>  effectivePacks = new ArrayList<>();

        String packName = String.join(" ", event.getArgs());

        for (Pack pack : packs) {
            if (pack.getName().equalsIgnoreCase(packName)) {
                pack.fetchTranslations(this.getEffectiveLanguage());
                message.editMessage(pack.getAsEmbed(event.getJDA()).build()).queue();
                return;
            }
            if (pack.getName().toLowerCase().contains(packName.toLowerCase())) {
                effectivePacks.add(pack);
            }
        }

        if (effectivePacks.size() == 0) {
            this.sendError(message, this.getTranslation(Translation.PACKS_NOTFOUND));
        } else if (effectivePacks.size() == 1) {
            Pack pack = effectivePacks.get(0);
            pack.fetchTranslations(this.getEffectiveLanguage());
            message.editMessage(pack.getAsEmbed(event.getJDA()).build()).queue();
        } else {
            new EmbedPage<Pack>(message, effectivePacks, 20) {

                @Override
                public EmbedBuilder getEmbed() {

                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(PackCommand.this.getTranslation(Translation.PACKS_LIST));
                    return builder;
                }
            };
        }
    }

    @Override
    public EmbedBuilder getAdvancedHelp() {

        EmbedBuilder builder = super.getAdvancedHelp();
        builder.setTitle("Advanced help for : pack");

        AdvancedHelpBuilder helpBuilder = new AdvancedHelpBuilder();

        helpBuilder.setDescription(this.getTranslation(this.getDescription()));


        AdvancedHelpSection usages = new AdvancedHelpSection("Usage");
        usages.addField("**`cm:pack <pack name>`**", "Search for a pack. Show the detail if only one is found.");

        AdvancedHelpSection examples = new AdvancedHelpSection("Example");
        examples.addField("`cm:pack Arachnophobia`", "Show the details for the Arachnophobia pack.");
        examples.addField("`cm:item ara`", "Show a list of pack containing **ara** in their name.");

        helpBuilder.addSection(usages);
        helpBuilder.addSection(examples);

        builder.setDescription(helpBuilder.toString());
        return builder;
    }

}
