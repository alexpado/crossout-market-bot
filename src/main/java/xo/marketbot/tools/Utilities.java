package xo.marketbot.tools;

import fr.alexpado.jda.interactions.meta.ChoiceMeta;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;
import xo.marketbot.entities.interfaces.common.Nameable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

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

    public static <V extends Nameable> List<ChoiceMeta> mapChoice(Map<Integer, V> mapping) {

        return mapChoice(mapping, Nameable::getName);
    }

    public static <V> List<ChoiceMeta> mapChoice(Map<Integer, V> mapping, Function<V, String> labelMapper) {

        return mapping.values().stream()
                .map(v -> new ChoiceMeta(labelMapper.apply(v), labelMapper.apply(v))).toList();

    }

}
