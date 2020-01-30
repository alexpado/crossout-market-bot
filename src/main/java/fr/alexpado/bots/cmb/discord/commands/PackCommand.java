package fr.alexpado.bots.cmb.discord.commands;

import fr.alexpado.bots.cmb.crossout.api.PackEnpoint;
import fr.alexpado.bots.cmb.crossout.models.Pack;
import fr.alexpado.bots.cmb.discord.BotCommand;
import fr.alexpado.bots.cmb.libs.jdamodules.JDAModule;
import fr.alexpado.bots.cmb.libs.jdamodules.events.CommandEvent;
import fr.alexpado.bots.cmb.tools.embed.EmbedPage;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.ArrayList;
import java.util.List;

public class PackCommand extends BotCommand {

    public PackCommand(JDAModule module) {
        super(module, "pack");
    }

    @Override
    public void runCommand(CommandEvent event) {
        this.sendWaiting(event, message -> {
            PackEnpoint endpoint = new PackEnpoint(this.getConfig().getApiHost());
            List<Pack> packs = endpoint.getAll();
            List<Pack> effectivePacks = new ArrayList<>();

            String packName = String.join(" ", event.getArgs());

            for (Pack pack : packs) {
                if (pack.getName().equalsIgnoreCase(packName)) {
                    message.editMessage(pack.getAsEmbed(event.getJDA()).build()).queue();
                    return;
                }

                if (pack.getName().toLowerCase().contains(packName.toLowerCase())) {
                    effectivePacks.add(pack);
                }
            }


            if (effectivePacks.size() == 0) {
                this.sendError(message, "No pack found !");
            } else if (effectivePacks.size() == 1) {
                message.editMessage(effectivePacks.get(0).getAsEmbed(event.getJDA()).build()).queue();
            } else {
                new EmbedPage<Pack>(message, effectivePacks, 20) {
                    @Override
                    public EmbedBuilder getEmbed() {
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setTitle("List of packs :");
                        return builder;
                    }
                };
            }
        });
    }
}
