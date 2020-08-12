package fr.alexpado.bots.cmb.cleaning.commands;

import fr.alexpado.bots.cmb.cleaning.XoDB;
import fr.alexpado.bots.cmb.cleaning.XoDBBot;
import fr.alexpado.bots.cmb.cleaning.entities.discord.ChannelEntity;
import fr.alexpado.bots.cmb.cleaning.entities.discord.GuildEntity;
import fr.alexpado.bots.cmb.cleaning.entities.discord.UserEntity;
import fr.alexpado.bots.cmb.cleaning.entities.i18n.messages.NoItemFound;
import fr.alexpado.bots.cmb.cleaning.i18n.TranslationProvider;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IItem;
import fr.alexpado.bots.cmb.cleaning.repositories.ChannelEntityRepository;
import fr.alexpado.bots.cmb.cleaning.repositories.GuildEntityRepository;
import fr.alexpado.bots.cmb.cleaning.repositories.UserEntityRepository;
import fr.alexpado.bots.cmb.cleaning.rest.exceptions.RequestException;
import fr.alexpado.jda.services.commands.DiscordCommand;
import fr.alexpado.jda.services.commands.annotations.Command;
import fr.alexpado.jda.services.commands.annotations.Flags;
import fr.alexpado.jda.services.commands.annotations.Param;
import fr.alexpado.jda.services.translations.Translator;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class ItemCommand extends DiscordCommand {

    private final XoDBBot                 bot;
    private final XoDB                    db;
    private final TranslationProvider     translationProvider;
    private final ChannelEntityRepository channelRepository;
    private final GuildEntityRepository   guildRepository;
    private final UserEntityRepository    userRepository;

    protected ItemCommand(XoDBBot discordBot, XoDB xoDB, TranslationProvider translationProvider, ChannelEntityRepository channelRepository, GuildEntityRepository guildRepository, UserEntityRepository userRepository) {

        super(discordBot);

        this.bot                 = discordBot;
        this.db                  = xoDB;
        this.translationProvider = translationProvider;
        this.channelRepository   = channelRepository;
        this.guildRepository     = guildRepository;
        this.userRepository      = userRepository;
    }

    @Command("name...")
    public void viewItemEmbed(GuildMessageReceivedEvent event, @Param("name") String itemName, @Flags List<String> flags) {

        event.getChannel().sendMessage(new EmbedBuilder().setDescription(":thinking:").build()).queue(message -> {

            UserEntity owner = this.userRepository.findById(event.getGuild().getOwnerIdLong())
                                                  .orElse(new UserEntity(event.getGuild().getOwner().getUser()));
            GuildEntity guild = this.guildRepository.findById(event.getGuild().getIdLong())
                                                    .orElse(new GuildEntity(event.getGuild(), owner));
            ChannelEntity channel = this.channelRepository.findByIdAndGuild(event.getChannel().getIdLong(), guild)
                                                          .orElse(new ChannelEntity(event.getChannel(), guild));


            Map<String, Object> searchParams = new HashMap<>();

            if (flags.contains("r")) {
                searchParams.put("removedItems", "true");
            }

            if (flags.contains("m")) {
                searchParams.put("metaItems", "true");
            }

            searchParams.put("query", itemName);
            searchParams.put("language", channel.getEffectiveLanguage());

            this.db.items().findAll(searchParams).queue(items -> {
                try {
                    if (items.isEmpty()) {
                        message.editMessage(this.emptyItemList(event.getJDA(), channel.getEffectiveLanguage()).build())
                               .queue();
                    } else if (items.size() == 1) {
                        message.editMessage(this.oneItemList(event.getJDA(), channel.getEffectiveLanguage(), items.get(0))
                                                .build()).queue();
                    } else {
                        message.editMessage(this.multipleItemList(event.getJDA(), channel.getEffectiveLanguage(), items, itemName)
                                                .build()).queue();
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }, throwable -> {
                if (!(throwable instanceof RequestException)) {
                    this.fatal(message, "Oof, something very bad happened.");
                }
            });
        });
    }

    private EmbedBuilder emptyItemList(JDA jda, String language) throws IllegalAccessException {

        NoItemFound feedback = new NoItemFound(jda);
        Translator.translate(this.translationProvider, language, feedback);
        return feedback;
    }

    private EmbedBuilder oneItemList(JDA jda, String language, IItem item) {

        return item.toEmbed(jda, language);
    }

    private EmbedBuilder multipleItemList(JDA jda, String language, List<IItem> items, String itemName) {

        Optional<IItem> optionalItem = items.stream().filter(i -> i.getName().equalsIgnoreCase(itemName)).findFirst();

        return optionalItem.map(item -> this.oneItemList(jda, language, item)).orElse(null);

    }

    private void fatal(Message message, String description) {

        message.editMessage(new EmbedBuilder().setColor(Color.RED)
                                              .setDescription(String.format("**Fatal Error:** %s", description))
                                              .build()).queue();
    }


}
