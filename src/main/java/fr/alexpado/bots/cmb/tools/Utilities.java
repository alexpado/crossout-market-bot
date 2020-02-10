package fr.alexpado.bots.cmb.tools;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Utilities {

    public static String money(int amount, String currency) {
        return String.format("%,.2f %s", amount / 100.0f, currency);
    }

    public static String money(float amount, String currency) {
        return String.format("%,.2f %s", amount, currency);
    }

    public static String removeHTML(String text) {
        Source htmlSource = new Source(text);
        Renderer htmlRend = new Renderer(htmlSource);
        return htmlRend.toString().replace("\n", "").replace("\r", "").replace(".", ". ");
    }

    public static String average(int value, int divider, String unit) {
        return String.format("%,.2f %s", (float) value / (float) divider, unit);
    }

    public static List<String> mergeList(List<String> array, String... elements) {
        List<String> result = new ArrayList<>();
        result.addAll(array);
        result.addAll(Arrays.asList(elements));
        return result;
    }
}
