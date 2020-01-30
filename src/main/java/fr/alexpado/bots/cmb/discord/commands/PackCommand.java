package fr.alexpado.bots.cmb.discord.commands;

import fr.alexpado.bots.cmb.crossout.api.PackEnpoint;
import fr.alexpado.bots.cmb.crossout.models.game.Pack;
import fr.alexpado.bots.cmb.discord.BotCommand;
import fr.alexpado.bots.cmb.libs.TKey;
import fr.alexpado.bots.cmb.libs.jdamodules.JDAModule;
import fr.alexpado.bots.cmb.libs.jdamodules.events.CommandEvent;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackCommand extends BotCommand {

    public PackCommand(JDAModule module) {
        super(module, "pack");
    }

    @Override
    public void execute(CommandEvent event, Message message) {
        PackEnpoint endpoint = new PackEnpoint(this.getConfig().getApiHost());
        List<Pack> packs = endpoint.getAll();
        List<Pack> effectivePacks = new ArrayList<>();

        String packName = String.join(" ", event.getArgs());

        for (Pack pack : packs) {
            if (pack.getName().equalsIgnoreCase(packName)) {
                pack.setTranslations(this.getTranslations());
                message.editMessage(pack.getAsEmbed(event.getJDA()).build()).queue();
                return;
            }
            if (pack.getName().toLowerCase().contains(packName.toLowerCase())) {
                effectivePacks.add(pack);
            }
        }

        if (effectivePacks.size() == 0) {
            this.sendError(message, this.getTranslation(TKey.PACK_NOT_FOUND));
        } else if (effectivePacks.size() == 1) {
            Pack pack = effectivePacks.get(0);
            pack.setTranslations(this.getTranslations());
            message.editMessage(pack.getAsEmbed(event.getJDA()).build()).queue();
        } else {
            new EmbedPage<Pack>(message, effectivePacks, 20) {
                @Override
                public EmbedBuilder getEmbed() {
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.setTitle(PackCommand.this.getTranslation(TKey.PACK_LIST));
                    return builder;
                }
            };
        }
    }

    @Override
    public List<String> getLanguageKeys() {
        return Arrays.asList(TKey.PACK_NOT_FOUND, TKey.PACK_LIST, TKey.ITEM_SELL, TKey.ITEM_BUY, TKey.DISCORD_INVITE);
    }
}
