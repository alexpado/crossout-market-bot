package xo.marketbot.tools;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DiscordEmbed extends EmbedBuilder {

    private static final Pattern EMBED_TITLE       = Pattern.compile("@title\\(\"(?<value>.*)\"\\)", Pattern.MULTILINE);
    private static final Pattern EMBED_DESCRIPTION = Pattern.compile("@description\\(\"(?<value>.*)\"\\)", Pattern.MULTILINE);
    private static final Pattern EMBED_THUMBNAIL   = Pattern.compile("@thumbnail\\(\"(?<value>.*)\"\\)", Pattern.MULTILINE);
    private static final Pattern EMBED_FIELD       = Pattern.compile("@field\\(\"(?<name>.*?)\", \"(?<value>.*?)\"\\)", Pattern.MULTILINE);
    private static final Pattern EMBED_COLOR       = Pattern.compile("@color\\((?<r>\\d*), (?<g>\\d*), (?<b>\\d*)\\)", Pattern.MULTILINE);

    private final JDA jda;

    public DiscordEmbed(JDA jda) {

        super();
        this.jda = jda;

        // Define the default footer
        super.setFooter(String.format("%s • Powered by CrossoutDB", jda.getSelfUser().getName()));
    }

    public JDA getJda() {

        return jda;
    }

    /**
     * Add data to the current embed by using a custom made markup language, allowing simple usage of translations.
     *
     * @param markup
     *         The markdown.
     */
    public void setMarkup(String markup) {

        Matcher titleMatcher       = EMBED_TITLE.matcher(markup);
        Matcher descriptionMatcher = EMBED_DESCRIPTION.matcher(markup);
        Matcher thumbnailMatcher   = EMBED_THUMBNAIL.matcher(markup);
        Matcher fieldMatcher       = EMBED_FIELD.matcher(markup);
        Matcher colorMatcher       = EMBED_COLOR.matcher(markup);

        if (titleMatcher.find()) {
            super.setTitle(titleMatcher.group("value"));
        }

        if (descriptionMatcher.find()) {
            super.setDescription(descriptionMatcher.group("value").replace("\\n", "\n"));
        }

        if (thumbnailMatcher.find()) {
            super.setThumbnail(descriptionMatcher.group("value"));
        }

        if (colorMatcher.find()) {
            int r = Integer.parseInt(colorMatcher.group("r"));
            int g = Integer.parseInt(colorMatcher.group("g"));
            int b = Integer.parseInt(colorMatcher.group("b"));

            super.setColor(new Color(r, g, b));
        }

        while (fieldMatcher.find()) {
            super.addField(fieldMatcher.group("name"), fieldMatcher.group("value").replace("\\n", "\n"), false);
        }

    }

}
