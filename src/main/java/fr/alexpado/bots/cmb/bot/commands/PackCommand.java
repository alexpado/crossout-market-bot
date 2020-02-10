package fr.alexpado.bots.cmb.bot.commands;

import fr.alexpado.bots.cmb.api.PackEnpoint;
import fr.alexpado.bots.cmb.interfaces.command.TranslatableBotCommand;
import fr.alexpado.bots.cmb.libs.jda.JDAModule;
import fr.alexpado.bots.cmb.libs.jda.events.CommandEvent;
import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.game.Pack;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;
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
        return Arrays.asList(
                Translation.PACKS_NOTFOUND,
                Translation.PACKS_LIST
        );
    }

    @Override
    public void execute(CommandEvent event, Message message) throws MissingTranslationException {
        PackEnpoint endpoint = new PackEnpoint(this.getConfig());
        List<Pack> packs = endpoint.getAll();
        List<Pack> effectivePacks = new ArrayList<>();

        String packName = String.join(" ", event.getArgs());

        for (Pack pack : packs) {
            if (pack.getName().equalsIgnoreCase(packName)) {
                pack.fetchTranslations(this.getDiscordGuild().getLanguage());
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
            pack.fetchTranslations(this.getDiscordGuild().getLanguage());
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

}
