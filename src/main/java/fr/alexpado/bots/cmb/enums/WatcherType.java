package fr.alexpado.bots.cmb.enums;

import fr.alexpado.bots.cmb.modules.crossout.models.Translation;

public enum WatcherType {

    SELL_OVER(1, Translation.WATCHERS_SELL_OVER),
    SELL_UNDER(2, Translation.WATCHERS_SELL_UNDER),
    BUY_OVER(3, Translation.WATCHERS_BUY_OVER),
    BUY_UNDER(4, Translation.WATCHERS_BUY_UNDER),
    NORMAL(5, Translation.WATCHERS_NORMAL),
    UNKNOWN(0, "");

    private final int id;
    private final String translation;

    WatcherType(int id, String translation) {
        this.id = id;
        this.translation = translation;
    }

    public static WatcherType getFromId(int id) {
        for (WatcherType value : WatcherType.values()) {
            if (value.getId() == id) {
                return value;
            }
        }
        return NORMAL;
    }

    public int getId() {
        return id;
    }

    public String getTranslation() {
        return translation;
    }
}
