package xo.marketbot.commands;

import fr.alexpado.jda.interactions.annotations.Choice;
import fr.alexpado.jda.interactions.annotations.Interact;
import fr.alexpado.jda.interactions.annotations.Option;
import fr.alexpado.jda.interactions.annotations.Param;
import fr.alexpado.jda.interactions.enums.SlashTarget;
import fr.alexpado.jda.interactions.responses.SlashResponse;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;
import xo.marketbot.entities.discord.Language;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.repositories.ChannelEntityRepository;
import xo.marketbot.repositories.GuildEntityRepository;
import xo.marketbot.repositories.UserEntityRepository;
import xo.marketbot.responses.SimpleMessageEmbed;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;
import xo.marketbot.services.interactions.InteractionBean;
import xo.marketbot.services.interactions.responses.SimpleSlashResponse;

import java.awt.*;
import java.util.Optional;
import java.util.function.Consumer;

import static xo.marketbot.services.i18n.TranslationService.*;

@InteractionBean
@Service
public class LanguageCommands {

    private static final Logger                  LOGGER = LoggerFactory.getLogger(LanguageCommands.class);
    private final        TranslationService      translationService;
    private final        UserEntityRepository    userEntityRepository;
    private final        ChannelEntityRepository channelEntityRepository;
    private final        GuildEntityRepository   guildEntityRepository;

    public LanguageCommands(TranslationService translationService, UserEntityRepository userEntityRepository, ChannelEntityRepository channelEntityRepository, GuildEntityRepository guildEntityRepository) {

        this.translationService      = translationService;
        this.userEntityRepository    = userEntityRepository;
        this.channelEntityRepository = channelEntityRepository;
        this.guildEntityRepository   = guildEntityRepository;
    }

    private Optional<SlashResponse> applyLanguageUpdate(TranslationContext context, ChannelEntity channel, Member member, JDA jda, String languageParam, Consumer<Language> languageConsumer) {

        Optional<Language> optionalLanguage = Optional.empty();

        if (!languageParam.equals("none")) {
            optionalLanguage = this.translationService.getSupportedLanguages().stream().filter(
                    language -> language.getId().equalsIgnoreCase(languageParam)
            ).findAny();
        }

        if (!member.isOwner() && !member.hasPermission(Permission.MANAGE_PERMISSIONS) && !member.hasPermission(Permission.ADMINISTRATOR)) {
            return Optional.of(new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_GENERAL__NOT_ALLOWED)));
        }

        if (optionalLanguage.isEmpty() && !languageParam.equals("none")) {
            return Optional.of(new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_LANGUAGE__NOT_SUPPORTED)));
        }

        languageConsumer.accept(optionalLanguage.orElse(null));
        return Optional.empty();
    }

    @Interact(
            name = "language/server",
            description = "Change the language of this server",
            target = SlashTarget.GUILD,
            options = {
                    @Option(
                            name = "language",
                            description = "The language",
                            type = OptionType.STRING,
                            required = true,
                            choices = {
                                    @fr.alexpado.jda.interactions.annotations.Choice(
                                            id = "en",
                                            display = "English (EN)"
                                    ),
                                    @fr.alexpado.jda.interactions.annotations.Choice(
                                            id = "fr",
                                            display = "Français (FR)"
                                    )
                            }
                    )
            }
    )
    public SlashResponse changeServerLanguage(GuildEntity guild, ChannelEntity channel, Member member, JDA jda, @Param("language") String languageParam) {

        TranslationContext      context                     = this.translationService.getContext(channel.getEffectiveLanguage());
        Optional<SlashResponse> optionalInteractionResponse = this.applyLanguageUpdate(context, channel, member, jda, languageParam, guild::setLanguage);

        if (optionalInteractionResponse.isPresent()) {
            return optionalInteractionResponse.get();
        }

        this.guildEntityRepository.save(guild);
        return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_LANGUAGE__UPDATED__GUILD));
    }

    @Interact(
            name = "language/channel",
            description = "Change the language of this channel",
            target = SlashTarget.GUILD,
            options = {
                    @Option(
                            name = "language",
                            description = "The language",
                            type = OptionType.STRING,
                            required = true,
                            choices = {
                                    @fr.alexpado.jda.interactions.annotations.Choice(
                                            id = "en",
                                            display = "English (EN)"
                                    ),
                                    @fr.alexpado.jda.interactions.annotations.Choice(
                                            id = "fr",
                                            display = "Français (FR)"
                                    ),
                                    @fr.alexpado.jda.interactions.annotations.Choice(
                                            id = "none",
                                            display = "Server's default"
                                    )
                            }
                    )
            }
    )
    public SlashResponse changeChannelLanguage(ChannelEntity channel, Member member, JDA jda, @Param("language") String languageParam) {

        TranslationContext      context                     = this.translationService.getContext(channel.getEffectiveLanguage());
        Optional<SlashResponse> optionalInteractionResponse = this.applyLanguageUpdate(context, channel, member, jda, languageParam, channel::setLanguage);

        if (optionalInteractionResponse.isPresent()) {
            return optionalInteractionResponse.get();
        }

        this.channelEntityRepository.save(channel);
        return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_LANGUAGE__UPDATED__CHANNEL));
    }

    @Interact(
            name = "language/user",
            description = "Change your own language",
            target = SlashTarget.ALL,
            options = {
                    @Option(
                            name = "language",
                            description = "The language",
                            type = OptionType.STRING,
                            required = true,
                            choices = {
                                    @fr.alexpado.jda.interactions.annotations.Choice(
                                            id = "en",
                                            display = "English (EN)"
                                    ),
                                    @Choice(
                                            id = "fr",
                                            display = "Français (FR)"
                                    )
                            }
                    )
            }
    )
    public SlashResponse changeUserLanguage(UserEntity user, JDA jda, @Param("language") String languageParam) {

        TranslationContext context = this.translationService.getContext(user.getLanguage());

        Optional<Language> optionalLanguage = this.translationService.getSupportedLanguages().stream().filter(
                language -> language.getId().equalsIgnoreCase(languageParam)
        ).findAny();

        if (optionalLanguage.isEmpty()) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.RED, TR_LANGUAGE__NOT_SUPPORTED));
        }

        Language language = optionalLanguage.get();
        user.setLanguage(language);
        this.userEntityRepository.save(user);
        return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.GREEN, TR_LANGUAGE__UPDATED__USER));
    }

}
