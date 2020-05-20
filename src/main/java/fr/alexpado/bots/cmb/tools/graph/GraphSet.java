package fr.alexpado.bots.cmb.tools.graph;

import java.util.ArrayList;
import java.util.List;

public class GraphSet {

    private final List<Integer> values;
    private final String setName;

    private final List<Integer> clampedValue = new ArrayList<>();

    private int min = Integer.MAX_VALUE;
    private int max = Integer.MIN_VALUE;


    public GraphSet(List<Integer> values, String setName) {
        this.values = values;

        for (Integer value : this.values) {
            this.min = Integer.min(value, this.min);
            this.max = Integer.max(value, this.max);
        }

        this.setName = setName;
    }

    public int getMax() {
        return max;
    }

    public int getMin() {
        return min;
    }

    public void clamp(int min, int max, int height) {
        this.values.forEach(value -> {
            double diff = max - min;
            this.clampedValue.add((int) (Math.round((height / diff) * (value - min))));
        });
    }

    public List<Integer> getClampedValue() {
        return clampedValue;
    }

    public String getSetName() {
        return setName;
    }


}
