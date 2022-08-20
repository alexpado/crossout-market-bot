package xo.marketbot.responses;

import net.dv8tion.jda.api.JDA;
import xo.marketbot.services.i18n.TranslationContext;

import java.awt.*;

public class SimpleMessageEmbed extends DisplayTemplate {

    public SimpleMessageEmbed(TranslationContext context, JDA jda, Color color, String key) {

        super(context, jda);
        this.setDescription(context.getTranslation(key));
        this.setColor(color);
    }

    public SimpleMessageEmbed(TranslationContext context, JDA jda, Color color, String key, Object... params) {

        super(context, jda);
        this.setDescription(String.format(context.getTranslation(key), params));
        this.setColor(color);
    }


}
