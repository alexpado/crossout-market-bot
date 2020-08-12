package fr.alexpado.bots.cmb.modules.crossout.models;

import fr.alexpado.bots.cmb.modules.crossout.models.keys.TranslationKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Getter
@IdClass(TranslationKey.class)
@Setter
public class OldTranslation {

    public static final String GENERAL_CURRENCY   = "general.currency";
    public static final String GENERAL_ERROR      = "general.error";
    public static final String GENERAL_INVITE     = "general.invite";
    public static final String GENERAL_BAD_SYNTAX = "general.syntax";
    public static final String GENERAL_FORBIDDEN  = "general.forbidden";
    public static final String XODB_OFFLINE       = "xodb.offline";

    public static final String ITEMS_LIST        = "items.list";
    public static final String ITEMS_MULTIPLE    = "items.multiple";
    public static final String ITEMS_NOTFOUND    = "items.notfound";
    public static final String ITEMS_REMOVED     = "items.removed";
    public static final String ITEMS_UNAVAILABLE = "items.unavailable";

    public static final String MARKET_BUY         = "market.buy";
    public static final String MARKET_CRAFTS_BUY  = "market.crafts.buy";
    public static final String MARKET_SELL        = "market.sell";
    public static final String MARKET_CRAFTS_SELL = "market.crafts.sell";

    public static final String PACKS_LIST     = "packs.list";
    public static final String PACKS_NOTFOUND = "packs.notfound";
    public static final String PACKS_PRICE    = "packs.price";

    public static final String LANGUAGES_LIST            = "languages.list";
    public static final String LANGUAGES_NOTSUPPORTED    = "languages.notsupported";
    public static final String LANGUAGES_GUILD_UPDATED   = "languages.updated.guild";
    public static final String LANGUAGES_CHANNEL_UPDATED = "languages.updated.channel";
    public static final String LANGUAGES_USER_UPDATED    = "languages.updated.user";

    public static final String WATCHERS_FORBIDDEN   = "watchers.forbidden";
    public static final String WATCHERS_LIST        = "watchers.list";
    public static final String WATCHERS_NEW         = "watchers.new";
    public static final String WATCHERS_NONE        = "watchers.none";
    public static final String WATCHERS_NOTFOUND    = "watchers.notfound";
    public static final String WATCHERS_PAUSED      = "watchers.paused";
    public static final String WATCHERS_REMOVED     = "watchers.removed";
    public static final String WATCHERS_RESUMED     = "watchers.resumed";
    public static final String WATCHERS_BUY_OVER    = "watchers.type.buy.over";
    public static final String WATCHERS_BUY_UNDER   = "watchers.type.buy.under";
    public static final String WATCHERS_NORMAL      = "watchers.type.normal";
    public static final String WATCHERS_OTHER       = "watchers.type.other";
    public static final String WATCHERS_SELL_OVER   = "watchers.type.sell.over";
    public static final String WATCHERS_SELL_UNDER  = "watchers.type.sell.under";
    public static final String WATCHERS_UNWATCH     = "watchers.unwatch";
    public static final String WATCHERS_UPDATED     = "watchers.updated";
    public static final String WATCHERS_WRONG_FOR   = "watchers.wrong.for";
    public static final String WATCHERS_WRONG_ID    = "watchers.wrong.id";
    public static final String WATCHERS_WRONG_PRICE = "watchers.wrong.price";
    public static final String WATCHERS_WRONG_TYPE  = "watchers.wrong.type";
    public static final String WATCHERS_WRONG_VALUE = "watchers.wrong.value";

    public static final String RARITIES_INVALID   = "rarities.invalid";
    public static final String CATEGORIES_INVALID = "categories.invalid";
    public static final String FACTIONS_INVALID   = "factions.invalid";
    public static final String TYPES_INVALID      = "types.invalid";

    public static final String COMMANDS_NOTFOUND = "commands.notfound";
    public static final String COMMANDS_NOHELP   = "commands.nohelp";

    public static final String HELP_DESCRIPTION = "help.description";

    @Id
    @Column(length = 100)
    private String translationKey;

    @Id
    @Column(length = 3)
    private String language;

    @NotNull
    private String text;

    @Override
    public boolean equals(Object o) {

        if (this == o) { return true; }
        if (!(o instanceof OldTranslation)) { return false; }
        OldTranslation that = (OldTranslation) o;
        return this.translationKey.equals(that.translationKey) && this.language.equals(that.language);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.translationKey, this.language);
    }

}
