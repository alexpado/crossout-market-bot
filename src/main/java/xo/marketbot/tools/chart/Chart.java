package xo.marketbot.tools.chart;

import xo.marketbot.configurations.interfaces.IChartConfiguration;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public class Chart {

    private final IChartConfiguration        configuration;
    private final BufferedImage              image;
    private final Graphics2D                 graphics;
    private final Map<String, List<Integer>> sets;
    private final Map<String, Color>         setColors;
    private final String                     name;

    private Integer minValue = Integer.MAX_VALUE;
    private Integer maxValue = Integer.MIN_VALUE;

    public Chart(IChartConfiguration configuration, BufferedImage image, String name) {

        this.configuration = configuration;
        this.image         = image;
        this.graphics      = this.image.createGraphics();
        this.name          = name;
        this.sets          = new HashMap<>();
        this.setColors     = new HashMap<>();

        // Enable anti-aliasing
        this.graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }

    public void addSet(String setName, Color color, List<Integer> setValues) {

        this.sets.put(setName, setValues);
        this.setColors.put(setName, color);

        setValues.forEach(value -> {
            this.minValue = Integer.min(this.minValue, value);
            this.maxValue = Integer.max(this.maxValue, value);
        });
    }

    private int clampValue(int value) {

        double height = this.configuration.getAreaHeight();
        double max    = this.maxValue;
        double min    = this.minValue;

        return (int) Math.round((height / (max - min)) * (value - min));
    }

    private void clampLists() {

        for (String key : this.sets.keySet()) {
            List<Integer> values = this.sets.get(key);
            values = values.stream().map(this::clampValue).collect(Collectors.toList());
            this.sets.put(key, values);
        }
    }

    public BufferedImage getChart() {

        this.drawChart();
        this.drawSets();

        this.graphics.dispose();
        return this.image;
    }

    private void drawSets() {

        int minX = configuration.getMinValueX();
        int minY = configuration.getMaxValueY();
        int maxX = configuration.getMaxValueX();
        int maxY = configuration.getMaxValueY();
        int txtX = configuration.getIntervalX();
        int txtY = configuration.getIntervalY();

        this.graphics.setFont(new Font("Ubuntu", Font.PLAIN, 20));
        this.graphics.setColor(Color.WHITE);

        this.graphics.drawString(String.format(Locale.US, "%.2f", this.minValue / 100.0), minX, minY);
        this.graphics.drawString(String.format(Locale.US, "%.2f", this.maxValue / 100.0), maxX, maxY);
        this.graphics.drawString(this.name, txtX, txtY);
    }

    private void drawChart() {

        this.clampLists();

        int graphX = configuration.getAreaX();
        int graphY = configuration.getAreaY();
        int graphH = configuration.getAreaHeight();
        int graphW = configuration.getAreaWidth();
        int stokeW = configuration.getStroke();

        this.graphics.setStroke(new BasicStroke(stokeW));

        this.sets.forEach((key, values) -> {
            this.graphics.setColor(this.setColors.get(key));

            float fX = (float) graphW / (values.size() - 1);

            int prevY = -1;
            int prevX = -1;

            for (int xVal = 0 ; xVal < values.size() ; xVal++) {
                int y  = graphY + graphH - values.get(xVal);
                int xn = Math.round(fX * (xVal));
                int x  = xn + graphX;

                if (prevY != -1) {
                    this.graphics.drawLine(prevX, prevY, x, y);
                }

                prevX = x;
                prevY = y;
            }
        });
    }


}
