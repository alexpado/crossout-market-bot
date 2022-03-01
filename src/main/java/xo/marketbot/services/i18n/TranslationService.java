package xo.marketbot.services.i18n;

import org.springframework.stereotype.Service;
import xo.marketbot.entities.discord.Language;
import xo.marketbot.entities.discord.Translation;
import xo.marketbot.repositories.LanguageRepository;
import xo.marketbot.repositories.TranslationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TranslationService {

    public static final String TR_EMBED__HEADER_FULL                    = "embed.header.full";
    public static final String TR_EMBED__HEADER_SIMPLE                  = "embed.header.simple";
    public static final String TR_EMBED__INVITE                         = "embed.invite";
    public static final String TR_EMBED__FOOTER_XODB                    = "embed.footer.xodb";
    public static final String TR_EMBED__FOOTER_DEVELOPER               = "embed.footer.developer";
    public static final String TR_EMBED__FOOTER_PAGINATION              = "embed.footer.pagination";
    public static final String TR_ITEM__REMOVED                         = "item.removed";
    public static final String TR_PACK__PRICE                           = "pack.price";
    public static final String TR_MARKET__CURRENCY                      = "market.currency";
    public static final String TR_MARKET__SELL                          = "market.sell";
    public static final String TR_MARKET__BUY                           = "market.buy";
    public static final String TR_MARKET__CRAFT_SELL                    = "market.craft.sell";
    public static final String TR_MARKET__CRAFT_BUY                     = "market.craft.buy";
    public static final String TR_SEARCH__EMPTY                         = "search.empty";
    public static final String TR_SEARCH__RESULTS                       = "search.results";
    public static final String TR_WATCHER__INVALID_PARAM__FREQUENCY     = "watcher.params.frequency";
    public static final String TR_WATCHER__INVALID_PARAM__PRICE         = "watcher.params.price";
    public static final String TR_WATCHER__INVALID_PARAM__PRICE_TRIGGER = "watcher.params.priceTrigger";
    public static final String TR_WATCHER__ITEM__TOO_MANY               = "watcher.item.tooMany";
    public static final String TR_WATCHER__ITEM__NONE                   = "watcher.item.none";
    public static final String TR_WATCHER__LIST_EMPTY                   = "watcher.empty";
    public static final String TR_WATCHER__NOT_FOUND                    = "watcher.notFound";
    public static final String TR_WATCHER__OWNERSHIP                    = "watcher.ownership";
    public static final String TR_WATCHER__CREATED                      = "watcher.created";
    public static final String TR_WATCHER__DELETED                      = "watcher.deleted";
    public static final String TR_WATCHER__UPDATED                      = "watcher.updated";
    public static final String TR_WATCHER__STATUS_PAUSED                = "watcher.status.paused";
    public static final String TR_WATCHER__STATUS_RESUMED               = "watcher.status.resumed";
    public static final String TR_WATCHER__ALREADY_PAUSED               = "watcher.already.paused";
    public static final String TR_WATCHER__ALREADY_RESUMED              = "watcher.already.resumed";
    public static final String TR_TRIGGER__SELL_UNDER                   = "trigger.sell.under";
    public static final String TR_TRIGGER__SELL_OVER                    = "trigger.sell.over";
    public static final String TR_TRIGGER__BUY_UNDER                    = "trigger.buy.under";
    public static final String TR_TRIGGER__BUY_OVER                     = "trigger.buy.over";
    public static final String TR_TRIGGER__EVERYTIME                    = "trigger.everytime";
    public static final String TR_LANGUAGE__UPDATED__GUILD              = "language.updated.guild";
    public static final String TR_LANGUAGE__UPDATED__CHANNEL            = "language.updated.channel";
    public static final String TR_LANGUAGE__UPDATED__USER               = "language.updated.user";
    public static final String TR_LANGUAGE__NOT_SUPPORTED               = "language.unsupported";
    public static final String TR_GENERAL__NOT_ALLOWED                  = "general.forbidden";
    public static final String TR_GENERAL__XODB_OFFLINE                 = "general.xodb.offline";
    public static final String TR_GENERAL__XODB_ERROR                   = "general.xodb.error";

    private final LanguageRepository    languageRepository;
    private final TranslationRepository translationRepository;

    public TranslationService(LanguageRepository languageRepository, TranslationRepository translationRepository) {

        this.languageRepository    = languageRepository;
        this.translationRepository = translationRepository;
    }

    public List<Language> getSupportedLanguages() {

        return this.languageRepository.findAll();
    }

    public TranslationContext getContext(Language language) {

        List<Translation>   translations   = this.translationRepository.findAllByLanguage(language);
        Map<String, String> translationMap = new HashMap<>();

        translations.forEach(translation -> translationMap.put(translation.getKey(), translation.getValue()));
        return new TranslationContext(translationMap);
    }

}
