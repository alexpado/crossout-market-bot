package xo.marketbot.helpers;

import fr.alexpado.xodb4j.XoDB;
import fr.alexpado.xodb4j.cache.EntityCache;
import fr.alexpado.xodb4j.interfaces.*;
import fr.alexpado.xodb4j.providers.composite.ItemProvider;
import xo.marketbot.entities.discord.Language;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CrossoutCache implements ItemProvider {

    private final XoDB                              xoDB;
    private final List<Language>                    apiLanguages;
    private final EntityCache<ICategory>            categoryCache;
    private final EntityCache<IFaction>             factionCache;
    private final EntityCache<IRarity>              rarityCache;
    private final EntityCache<IType>                typeCache;
    private final EntityCache<IPack>                packCache;
    private final Map<Language, EntityCache<IItem>> translatedItemCache;

    public CrossoutCache(XoDB xoDB, List<Language> apiLanguages) throws Exception {

        this.xoDB                = xoDB;
        this.apiLanguages        = apiLanguages;
        this.categoryCache       = new EntityCache<>(86_460_000);
        this.factionCache        = new EntityCache<>(86_460_000);
        this.rarityCache         = new EntityCache<>(86_460_000);
        this.typeCache           = new EntityCache<>(86_460_000);
        this.packCache           = new EntityCache<>(86_460_000);
        this.translatedItemCache = new HashMap<>();

        // Register cache
        this.xoDB.setProvider(this);
        // Build cache
        this.cache();
    }

    public void cache() throws Exception {

        this.categoryCache.store(this.xoDB.categories().findAll().complete());
        this.factionCache.store(this.xoDB.factions().findAll().complete());
        this.rarityCache.store(this.xoDB.rarities().findAll().complete());
        this.typeCache.store(this.xoDB.types().findAll().complete());
        this.packCache.store(this.xoDB.packs().findAll().complete());

        for (Language language : this.apiLanguages) {
            this.translatedItemCache.computeIfAbsent(language, (lang) -> new EntityCache<>(86_460_000));

            EntityCache<IItem> itemCache = this.translatedItemCache.get(language);
            itemCache.store(this.xoDB.items().findAll(new HashMap<>() {{
                this.put("language", language.getId());
            }}).complete());
        }
    }

    @Override
    public ICategory provideCategory(Integer id) {

        // Hacky query to no contact the API.
        return this.categoryCache.query(elem -> elem.getId().equals(id), () -> ICategory.DEFAULT);
    }

    @Override
    public IFaction provideFaction(Integer id) {

        // Hacky query to no contact the API.
        return this.factionCache.query(elem -> elem.getId().equals(id), () -> IFaction.DEFAULT);
    }

    @Override
    public IRarity provideRarity(Integer id) {

        // Hacky query to no contact the API.
        return this.rarityCache.query(elem -> elem.getId().equals(id), () -> IRarity.DEFAULT);
    }

    @Override
    public IType provideType(Integer id) {

        // Hacky query to no contact the API.
        return this.typeCache.query(elem -> elem.getId().equals(id), () -> IType.DEFAULT);
    }

    public List<ICategory> getCategories() {

        return this.categoryCache.queryAll(elem -> true);
    }

    public List<IFaction> getFactions() {

        return this.factionCache.queryAll(elem -> true);
    }

    public List<IRarity> getRarities() {

        return this.rarityCache.queryAll(elem -> true);
    }

    public List<IType> getTypes() {

        return this.typeCache.queryAll(elem -> true);
    }

    public List<IPack> getPacks() {

        return this.packCache.queryAll(elem -> true);
    }

    public List<IItem> getItems(String languageId, String defaultLanguage) {

        return this.apiLanguages.stream()
                                .filter(language -> language.getId().equals(languageId))
                                .findFirst()
                                .map(this.translatedItemCache::get)
                                .map(cache -> cache.queryAll(elem -> true))
                                .orElseGet(() -> this.apiLanguages.stream()
                                                                  .filter(language -> language.getId()
                                                                                              .equals(defaultLanguage))
                                                                  .findFirst()
                                                                  .map(this.translatedItemCache::get)
                                                                  .map(cache -> cache.queryAll(elem -> true))
                                                                  .orElse(Collections.emptyList()));
    }

}
