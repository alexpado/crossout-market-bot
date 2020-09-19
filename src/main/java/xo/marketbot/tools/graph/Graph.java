package xo.marketbot.tools.graph;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Graph {

    private final int height = 100;
    private final int width  = 400;

    private final int            margin = 20;
    private final List<GraphSet> sets   = new ArrayList<>();
    private       int            max    = Integer.MIN_VALUE;
    private       int            min    = Integer.MAX_VALUE;

    public Graph(GraphSet... valuesSets) {

        for (GraphSet set : valuesSets) {
            this.sets.add(set);
            this.max = Integer.max(set.getMax(), this.max);
            this.min = Integer.min(set.getMin(), this.min);
        }
    }

    public void clamp(int height) {

        this.sets.forEach(set -> {
            set.clamp(this.min, this.max, height);
        });
    }

    public void forceMin(int min) {

        this.min = min;
    }

    public void forceMax(int max) {

        this.max = max;
    }

    public int getMin() {

        return this.min;
    }

    public int getMax() {

        return this.max;
    }

    public List<GraphSet> getSets() {

        return this.sets;
    }

    public BufferedImage draw(Color... colors) throws Exception {

        BufferedImage image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_4BYTE_ABGR);
        Graphics2D    _2d   = image.createGraphics();

        _2d.setColor(new Color(255, 255, 255, 15));

        _2d.fillRect(this.margin + 1, this.margin, this.width - this.margin * 2, this.height - this.margin * 2);

        _2d.setColor(Color.WHITE);

        _2d.setStroke(new BasicStroke(3));

        _2d.drawLine(this.margin, this.margin, this.margin, this.height - this.margin);
        _2d.drawLine(this.margin, this.height - this.margin, this.width - this.margin, this.height - this.margin);

        for (int i = 0 ; i < this.sets.size() ; i++) {
            if (colors.length > i) {
                _2d.setColor(colors[i]);

                if (i == 0) {
                    _2d.drawString("— " + this.sets.get(i).getSetName(), this.margin * 5, this.height);
                } else if (i == 1) {
                    _2d.drawString("— " + this.sets.get(i).getSetName(), this.margin * 8, this.height);
                }
            }

            List<Integer> valuesSet = this.sets.get(i).getClampedValue();

            for (int xVal = 1 ; xVal < valuesSet.size() ; xVal++) {
                int y1 = (this.height - this.margin) - valuesSet.get(xVal - 1);
                int y2 = (this.height - this.margin) - valuesSet.get(xVal);

                int x1 = ((int) (Math.round(((this.width - this.margin * 2.0) / valuesSet.size()) * (xVal - 1)) + this.margin));
                int x2 = ((int) (Math.round(((this.width - this.margin * 2.0) / valuesSet.size()) * (xVal)) + this.margin));

                _2d.drawLine(x1, y1, x2, y2);
            }
        }

        _2d.setColor(Color.WHITE);

        _2d.drawString("Last 5 hours", this.margin * 12, this.margin / 2);
        _2d.drawString(Double.toString(this.max / 100.0), this.margin, this.margin / 2);
        _2d.drawString(Double.toString(this.min / 100.0), this.margin, this.height);


        _2d.dispose();

        return image;
    }


}
