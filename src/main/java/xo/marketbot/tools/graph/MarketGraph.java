package xo.marketbot.tools.graph;

import fr.alexpado.bots.cmb.tools.JSONConfiguration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Locale;

public class MarketGraph extends Graph {

    private final String graphName;

    public MarketGraph(String graphName, GraphSet... valuesSets) {

        super(valuesSets);
        this.graphName = graphName;
    }

    @Override
    public BufferedImage draw(Color... colors) throws Exception {
        // Load the configuration file
        JSONConfiguration configuration = new JSONConfiguration(new File("graph.json"));

        // Load the blueprint picture.
        BufferedImage image = ImageIO.read(new File(configuration.getString("source")));
        Graphics2D    _2d   = image.createGraphics();

        _2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        this.drawGraph(_2d, configuration, colors);
        this.drawSets(_2d, configuration);

        _2d.dispose();
        return image;
    }

    private void drawSets(Graphics2D _2d, JSONConfiguration configuration) {

        int minX = configuration.getInt("map.minValue.x");
        int minY = configuration.getInt("map.minValue.y");
        int maxX = configuration.getInt("map.maxValue.x");
        int maxY = configuration.getInt("map.maxValue.y");
        int txtX = configuration.getInt("map.intervalText.x");
        int txtY = configuration.getInt("map.intervalText.y");

        _2d.setFont(new Font("Ubuntu", Font.PLAIN, 20));

        _2d.setColor(Color.WHITE);

        _2d.drawString(String.format(Locale.US, "%.2f", this.getMin() / 100.0), minX, minY);
        _2d.drawString(String.format(Locale.US, "%.2f", this.getMax() / 100.0), maxX, maxY);
        _2d.drawString(this.graphName, txtX, txtY);

    }

    private void drawGraph(Graphics2D _2d, JSONConfiguration configuration, Color... colors) {

        int graphX = configuration.getInt("map.graph.x");
        int graphY = configuration.getInt("map.graph.y");
        int graphH = configuration.getInt("map.graph.h");
        int graphW = configuration.getInt("map.graph.w");
        int stokeW = configuration.getInt("map.graph.s");

        this.clamp(graphH);

        // Set the stoke width for the graph.
        _2d.setStroke(new BasicStroke(stokeW));

        for (int i = 0 ; i < this.getSets().size() ; i++) {
            GraphSet set = this.getSets().get(i);
            if (colors.length > i) {
                _2d.setColor(colors[i]);
            }

            List<Integer> values = set.getClampedValue();

            float fX = (float) graphW / (values.size() - 1);

            int prevY = -1;
            int prevX = -1;

            for (int xVal = 0 ; xVal < values.size() ; xVal++) {
                int y  = graphY + graphH - values.get(xVal);
                int xn = Math.round(fX * (xVal));
                int x  = xn + graphX;

                if (prevY != -1) {
                    _2d.drawLine(prevX, prevY, x, y);
                }

                prevX = x;
                prevY = y;
            }
        }
    }

}
