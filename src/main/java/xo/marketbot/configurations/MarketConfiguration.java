package xo.marketbot.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import xo.marketbot.configurations.interfaces.IMarketConfiguration;

@Configuration
@PropertySources({
        @PropertySource(value = "file:./xomarket.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:xomarket.properties", ignoreResourceNotFound = true)
})
public class MarketConfiguration implements IMarketConfiguration {

    @Value("${xomarket.api}")
    private String marketApi;

    @Value("${xomarket.hostname}")
    private String hostname;

    @Value("${xomarket.defaults.watcher-timing}")
    private Integer watcherTiming;

    @Value("${xomarket.defaults.language}")
    private String defaultLanguage;

    @Value("${xomarket.defaults.chart-interval}")
    private Integer chartInterval;

    @Override
    public String getApi() {

        return this.marketApi;
    }

    @Override
    public String getHostname() {

        return this.hostname;
    }

    @Override
    public Integer getDefaultWatcherTiming() {

        return this.watcherTiming;
    }

    @Override
    public String getDefaultLanguage() {

        return this.defaultLanguage;
    }

    @Override
    public Integer getDefaultChartInterval() {

        return this.chartInterval;
    }
}
