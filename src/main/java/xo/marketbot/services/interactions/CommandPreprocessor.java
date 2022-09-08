package xo.marketbot.services.interactions;

import fr.alexpado.jda.interactions.entities.DispatchEvent;
import fr.alexpado.jda.interactions.interfaces.interactions.InteractionPreprocessor;
import net.dv8tion.jda.api.entities.Channel;
import net.dv8tion.jda.api.interactions.Interaction;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.Language;
import xo.marketbot.repositories.ChannelEntityRepository;
import xo.marketbot.responses.SimpleMessageEmbed;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;
import xo.marketbot.services.interactions.responses.SimpleSlashResponse;
import xo.marketbot.tasks.XoHealthCheckTask;

import java.awt.*;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static xo.marketbot.services.i18n.TranslationService.TR_GENERAL__XODB_OFFLINE;

@Service
public class CommandPreprocessor implements InteractionPreprocessor {

    private static final List<String>            INTERACTION_XODB_RELATED = Arrays.asList(
            "search/item",
            "search/pack",
            "watchers/create"
    );
    private final        XoHealthCheckTask       task;
    private final        TranslationService      translationService;
    private final        ChannelEntityRepository channelRepository;

    public CommandPreprocessor(XoHealthCheckTask task, TranslationService translationService, ChannelEntityRepository channelRepository) {

        this.task               = task;
        this.translationService = translationService;
        this.channelRepository  = channelRepository;
    }

    @Override
    public <T extends Interaction> Optional<Object> preprocess(@NotNull DispatchEvent<T> dispatchEvent) {

        Channel  channel  = dispatchEvent.getInteraction().getChannel();
        Language language = this.translationService.getSupportedLanguages().get(0);

        if (channel != null) {
            language = this.channelRepository.findById(channel.getIdLong())
                                             .map(ChannelEntity::getEffectiveLanguage)
                                             .orElse(language);
        }

        TranslationContext context = this.translationService.getContext(language);

        URI    interaction = dispatchEvent.getPath();
        String action      = "%s%s".formatted(interaction.getHost(), interaction.getPath());

        if (INTERACTION_XODB_RELATED.contains(action)) {
            return Optional.of(
                    new SimpleSlashResponse(
                            new SimpleMessageEmbed(
                                    context,
                                    dispatchEvent.getInteraction().getJDA(),
                                    Color.RED,
                                    TR_GENERAL__XODB_OFFLINE
                            )
                    )
            );
        }

        return Optional.empty();
    }

}
