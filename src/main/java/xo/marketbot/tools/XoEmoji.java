package xo.marketbot.tools;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.Guild;
import xo.marketbot.XoMarketApplication;

public class XoEmoji {

    public static final long DOWN_GREEN = 932068431925227580L;
    public static final long DOWN_RED   = 932068431925227580L;
    public static final long UP_GREEN   = 932068431925227580L;
    public static final long UP_RED     = 932068431925227580L;
    public static final long FLAT       = 932068431925227580L;

    public static Emote getDownGreen(JDA jda) {

        return load(jda, DOWN_GREEN);
    }

    public static Emote getDownRed(JDA jda) {

        return load(jda, DOWN_RED);
    }

    public static Emote getUpGreen(JDA jda) {

        return load(jda, UP_GREEN);
    }

    public static Emote getUpRed(JDA jda) {

        return load(jda, UP_RED);
    }

    public static Emote getFlat(JDA jda) {

        return load(jda, FLAT);
    }

    public static Emote load(JDA jda, long id) {

        Guild guild = jda.getGuildById(XoMarketApplication.BOT_OFFICIAL_SERVER_ID);

        if (guild == null) {
            throw new NullPointerException("Unable to find emoji.");
        }

        Emote emoteById = guild.getEmoteById(id);
        if (emoteById == null) {
            throw new NullPointerException("Unable to find emoji.");
        }
        return emoteById;
    }
}
