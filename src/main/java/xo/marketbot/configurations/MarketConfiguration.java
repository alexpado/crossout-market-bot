package xo.marketbot.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import xo.marketbot.configurations.interfaces.IMarketConfiguration;

@Configuration
public class MarketConfiguration implements IMarketConfiguration {

    @Value("${xomarket.api}")
    private String marketApi;

    @Value("${xomarket.chart}")
    private String chartApi;

    @Override
    public String getApi() {

        return this.marketApi;
    }

    @Override
    public String getChartApi() {

        return this.chartApi;
    }

}
