package xo.marketbot.enums;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import xo.marketbot.XoMarketApplication;

public enum PriceEmote {

    STALE(574619458337898497L),
    DOWN_RED(574619458371584003L),
    DOWN_GREEN(574619457914404897L),
    UP_RED(574619458145091604L),
    UP_GREEN(574619458606202889L);

    private final long id;

    PriceEmote(long id) {

        this.id = id;
    }

    public static PriceEmote with(double newValue, double oldValue, boolean downIsBad) {

        if (newValue == oldValue) {
            return STALE;
        } else if (newValue < oldValue) {
            return downIsBad ? DOWN_RED : DOWN_GREEN;
        } else {
            return downIsBad ? UP_GREEN : UP_RED;
        }
    }

    public long getId() {

        return id;
    }

    public String getEmote(JDA jda) {

        Emote globalEmoteById = jda.getEmoteById(this.id);
        if (globalEmoteById != null) {return globalEmoteById.getAsMention();}
        Guild guild = jda.getGuildById(XoMarketApplication.BOT_OFFICIAL_SERVER_ID);
        if (guild != null) {
            Emote guildEmoteById = guild.getEmoteById(this.id);
            if (guildEmoteById != null) {return guildEmoteById.getAsMention();}
            return guild.retrieveEmoteById(this.id).complete().getAsMention();
        }
        return "";
    }
}
