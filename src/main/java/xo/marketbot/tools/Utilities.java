package xo.marketbot.tools;

import fr.alexpado.jda.interactions.meta.ChoiceMeta;
import fr.alexpado.xodb4j.interfaces.IItem;
import fr.alexpado.xodb4j.interfaces.common.Nameable;
import net.dv8tion.jda.api.JDA;
import net.htmlparser.jericho.Renderer;
import net.htmlparser.jericho.Source;
import xo.marketbot.configurations.interfaces.IMarketConfiguration;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

    public static String createInvitationLink(JDA jda) {

        String url = "https://discord.com/api/oauth2/authorize?client_id=%s&permissions=262144&scope=bot";
        return url.formatted(jda.getSelfUser().getId());
    }

    public static String createChartUrl(IMarketConfiguration configuration, IItem item, int interval) {

        String chartApi  = configuration.getChartApi();
        long   timestamp = item.getLastUpdate().toEpochSecond(ZoneOffset.UTC);

        return "%s/chart/%s/%s.png?hour=%s".formatted(chartApi, timestamp, item.getId(), interval);
    }

}
