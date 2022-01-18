package xo.marketbot.enums;

import static xo.marketbot.services.i18n.TranslationService.*;

public enum WatcherTrigger {

    SELL_UNDER(true, TR_TRIGGER__SELL_UNDER),
    SELL_OVER(true, TR_TRIGGER__SELL_OVER),
    BUY_UNDER(true, TR_TRIGGER__BUY_UNDER),
    BUY_OVER(true, TR_TRIGGER__BUY_OVER),
    EVERYTIME(false, TR_TRIGGER__EVERYTIME);

    private final boolean requirePrice;
    private final String  translationKey;

    WatcherTrigger(boolean requirePrice, String translationKey) {

        this.requirePrice   = requirePrice;
        this.translationKey = translationKey;
    }

    public static WatcherTrigger from(String value) {

        return valueOf(value.toUpperCase());
    }

    public String getTranslationKey() {

        return translationKey;
    }

    public boolean isRequiringPrice() {

        return requirePrice;
    }
}
