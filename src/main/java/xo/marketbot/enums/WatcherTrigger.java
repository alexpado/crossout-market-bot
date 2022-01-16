package xo.marketbot.enums;

public enum WatcherTrigger {

    SELL_UNDER(true),
    SELL_OVER(true),
    BUY_UNDER(true),
    BUY_OVER(true),
    EVERYTIME(false);

    private final boolean requirePrice;

    WatcherTrigger(boolean requirePrice) {

        this.requirePrice = requirePrice;
    }

    public static WatcherTrigger from(String value) {

        return valueOf(value.toUpperCase());
    }

    public boolean isRequiringPrice() {

        return requirePrice;
    }
}
