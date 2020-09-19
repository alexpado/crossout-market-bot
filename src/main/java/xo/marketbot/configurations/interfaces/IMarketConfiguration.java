package xo.marketbot.configurations.interfaces;

public interface IMarketConfiguration {

    String getApi();

    String getHostname();

    Integer getDefaultWatcherTiming();

    String getDefaultLanguage();

    Integer getDefaultChartInterval();

}
