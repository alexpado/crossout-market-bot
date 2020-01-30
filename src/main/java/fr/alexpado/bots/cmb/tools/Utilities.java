package fr.alexpado.bots.cmb.tools;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;

public class Utilities {

    public static String money(int amount, String currency) {
        return String.format("%,.2f %s", amount / 100.0f, currency);
    }

    public static String removeHTML(String text) {
        Source htmlSource = new Source(text);
        Renderer htmlRend = new Renderer(htmlSource);
        return htmlRend.toString().replace("\n", "").replace("\r", "").replace(".", ". ");
    }

}
