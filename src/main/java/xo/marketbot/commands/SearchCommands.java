package xo.marketbot.commands;

import fr.alexpado.jda.interactions.annotations.Interact;
import fr.alexpado.jda.interactions.annotations.Option;
import fr.alexpado.jda.interactions.annotations.Param;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xo.marketbot.entities.discord.CachedItem;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.interfaces.game.IItem;
import xo.marketbot.entities.interfaces.game.IPack;
import xo.marketbot.repositories.CachedItemRepository;
import xo.marketbot.responses.EntitiesDisplay;
import xo.marketbot.responses.EntityDisplay;
import xo.marketbot.responses.SimpleMessageEmbed;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;
import xo.marketbot.services.interactions.InteractionBean;
import xo.marketbot.services.interactions.pagination.PaginationTarget;
import xo.marketbot.services.interactions.responses.SimpleSlashResponse;
import xo.marketbot.tools.SearchHelper;
import xo.marketbot.xodb.XoDB;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static xo.marketbot.services.i18n.TranslationService.TR_SEARCH__EMPTY;

@InteractionBean
@Service
public class SearchCommands {

    private static final Logger               LOGGER = LoggerFactory.getLogger(SearchCommands.class);
    private final        XoDB                 xoDB;
    private final        TranslationService   translationService;
    private final        CachedItemRepository cacheRepository;

    public SearchCommands(XoDB xoDB, TranslationService translationService, CachedItemRepository cacheRepository) {

        this.xoDB               = xoDB;
        this.translationService = translationService;
        this.cacheRepository    = cacheRepository;
    }

    @Interact(
            name = "search/item",
            description = "Display an item detail",
            options = {
                    @Option(
                            name = "item",
                            description = "Name of the item (complete or not)",
                            type = OptionType.STRING,
                            autoComplete = true
                    ),
                    @Option(
                            name = "meta",
                            description = "Include meta items",
                            type = OptionType.BOOLEAN
                    ),
                    @Option(
                            name = "removed",
                            description = "Include removed items",
                            type = OptionType.BOOLEAN
                    ),
                    @Option(
                            name = "category",
                            description = "Category from which the item must be part of",
                            type = OptionType.STRING,
                            autoComplete = true
                    ),
                    @Option(
                            name = "rarity",
                            description = "Rarity from which the item must be part of",
                            type = OptionType.STRING,
                            autoComplete = true
                    ),
                    @Option(
                            name = "faction",
                            description = "Faction from which the item must be part of",
                            type = OptionType.STRING,
                            autoComplete = true
                    ),
            }
    )
    public Object searchItem(
            JDA jda,
            ChannelEntity channel,
            @Param("item") String itemId,
            @Param("rarity") String rarityNameParam,
            @Param("category") String categoryNameParam,
            @Param("faction") String factionNameParam,
            @Param("meta") Boolean metaParam,
            @Param("removed") Boolean removedParam
    ) throws Exception {

        TranslationContext  context      = this.translationService.getContext(channel.getEffectiveLanguage());
        Map<String, Object> searchParams = new HashMap<>();

        if (itemId != null) {
            int                  id   = Integer.parseInt(itemId);
            Optional<CachedItem> byId = this.cacheRepository.findById(id);
            byId.ifPresent(cachedItem -> {
                searchParams.put("query", cachedItem.getName());
                searchParams.put("rarity", cachedItem.getRarity());
                searchParams.put("category", cachedItem.getCategory());
                searchParams.put("faction", cachedItem.getFaction());
                searchParams.put("metaItems", cachedItem.isMeta());
                searchParams.put("removedItems", cachedItem.isRemoved());
            });
        }

        if (searchParams.isEmpty()) {
            Optional.ofNullable(rarityNameParam).ifPresent(str -> searchParams.put("rarity", str));
            Optional.ofNullable(categoryNameParam).ifPresent(str -> searchParams.put("category", str));
            Optional.ofNullable(factionNameParam).ifPresent(str -> searchParams.put("faction", str));

            searchParams.put("metaItems", metaParam != null && metaParam);
            searchParams.put("removedItems", removedParam != null && removedParam);
        }

        searchParams.put("language", channel.getEffectiveLanguage());

        List<IItem> items = this.xoDB.items().findAll(searchParams).complete();

        if (items.isEmpty()) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_SEARCH__EMPTY));
        }

        Optional<IItem> search = SearchHelper.search(items, null);

        if (search.isPresent()) {
            return new SimpleSlashResponse(new EntityDisplay(context, jda, this.xoDB, search.get()));
        }
        return new PaginationTarget(new EntitiesDisplay<>(context, jda, items));
    }

    @Interact(
            name = "search/pack",
            description = "Display a pack detail",
            options = {
                    @Option(
                            name = "pack",
                            description = "Name of the pack (complete or not)",
                            type = OptionType.STRING,
                            autoComplete = true
                    )
            }
    )
    public Object searchPack(JDA jda, ChannelEntity channel, @Param("pack") String packName) throws Exception {

        TranslationContext context = this.translationService.getContext(channel.getEffectiveLanguage());

        List<IPack> packs = this.xoDB.packs().findAll().complete();

        if (packs.isEmpty()) {
            return new SimpleSlashResponse(new SimpleMessageEmbed(context, jda, Color.ORANGE, TR_SEARCH__EMPTY));
        }

        Optional<IPack> search = SearchHelper.search(packs, packName);

        if (search.isPresent()) {
            return new SimpleSlashResponse(new EntityDisplay(context, jda, search.get()));
        }
        return new PaginationTarget(new EntitiesDisplay<>(context, jda, packs));
    }

}
