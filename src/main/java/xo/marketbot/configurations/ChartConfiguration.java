package xo.marketbot.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import xo.marketbot.configurations.interfaces.IChartConfiguration;

@Configuration
@PropertySources({
        @PropertySource(value = "file:./chart.properties", ignoreResourceNotFound = true),
        @PropertySource(value = "classpath:chart.properties", ignoreResourceNotFound = true)
})
public class ChartConfiguration implements IChartConfiguration {

    @Value("${chart.source}")
    private String source;

    @Value("${chart.labels.min-value.x}")
    private Integer minValueX;

    @Value("${chart.labels.min-value.y}")
    private Integer minValueY;

    @Value("${chart.labels.max-value.x}")
    private Integer maxValueX;

    @Value("${chart.labels.max-value.y}")
    private Integer maxValueY;

    @Value("${chart.labels.interval.x}")
    private Integer intervalX;

    @Value("${chart.labels.interval.y}")
    private Integer intervalY;

    @Value("${chart.area.width}")
    private Integer areaWidth;

    @Value("${chart.area.height}")
    private Integer areaHeight;

    @Value("${chart.area.x}")
    private Integer areaX;

    @Value("${chart.area.y}")
    private Integer areaY;

    @Value("${chart.stroke}")
    private Integer stoke;


    @Override
    public String getSource() {

        return this.source;
    }

    @Override
    public Integer getMinValueX() {

        return this.minValueX;
    }

    @Override
    public Integer getMinValueY() {

        return this.minValueY;
    }

    @Override
    public Integer getMaxValueX() {

        return this.maxValueX;
    }

    @Override
    public Integer getMaxValueY() {

        return this.maxValueY;
    }

    @Override
    public Integer getIntervalX() {

        return this.intervalX;
    }

    @Override
    public Integer getIntervalY() {

        return this.intervalY;
    }

    @Override
    public Integer getAreaWidth() {

        return this.areaWidth;
    }

    @Override
    public Integer getAreaHeight() {

        return this.areaHeight;
    }

    @Override
    public Integer getAreaX() {

        return this.areaX;
    }

    @Override
    public Integer getAreaY() {

        return this.areaY;
    }

    @Override
    public Integer getStroke() {

        return this.stoke;
    }
}
