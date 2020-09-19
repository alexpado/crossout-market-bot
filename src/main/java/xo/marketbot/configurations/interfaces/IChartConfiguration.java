package xo.marketbot.configurations.interfaces;

public interface IChartConfiguration {

    String getSource();

    Integer getMinValueX();

    Integer getMinValueY();

    Integer getMaxValueX();

    Integer getMaxValueY();

    Integer getIntervalX();

    Integer getIntervalY();

    Integer getAreaWidth();

    Integer getAreaHeight();

    Integer getAreaX();

    Integer getAreaY();

    Integer getStroke();

}
