package xo.marketbot.tools;

import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;

import java.time.LocalDateTime;

public class Utilities {

    public static String money(double amount, String currency) {

        return String.format("%,.2f %s", amount, currency);
    }

    public static String removeHTML(String text) {

        Source   htmlSource = new Source(text);
        Renderer htmlRend   = new Renderer(htmlSource);
        return htmlRend.toString().replace("\n", "").replace("\r", "").replace(".", ". ");
    }

    public static LocalDateTime toNormalizedDateTime(LocalDateTime other, long seconds) {

        long minutes      = seconds / 60;
        long unnormalized = (other.getHour() * 60) + other.getMinute();

        long normalizedMinutes = unnormalized % minutes;

        return other.minusMinutes(normalizedMinutes)
                    .withSecond(0)
                    .withNano(0);
    }

}
