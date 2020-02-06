package fr.alexpado.bots.cmb.enums;

import fr.alexpado.bots.cmb.models.Translation;

public enum WatcherTypes {

    SELL_OVER(1, Translation.WATCHERS_SELL_OVER),
    SELL_UNDER(2, Translation.WATCHERS_SELL_UNDER),
    BUY_OVER(3, Translation.WATCHERS_BUY_OVER),
    BUY_UNDER(4, Translation.WATCHERS_BUY_UNDER),
    NORMAL(5, Translation.WATCHERS_NORMAL);

    private int id;
    private String translation;

    WatcherTypes(int id, String translation) {
        this.id = id;
        this.translation = translation;
    }

    public static WatcherTypes getFromId(int id) {
        for (WatcherTypes value : WatcherTypes.values()) {
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
