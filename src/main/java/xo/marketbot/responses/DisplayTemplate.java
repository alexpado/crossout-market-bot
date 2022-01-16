package xo.marketbot.responses;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xo.marketbot.XoMarketApplication;

public class DisplayTemplate extends EmbedBuilder {

    private final JDA    jda;
    private final String title;

    public DisplayTemplate(JDA jda) {

        this.jda   = jda;
        this.title = null;
        this.makeDefaults();
    }

    public DisplayTemplate(JDA jda, String title) {

        this.jda   = jda;
        this.title = title;
        this.makeDefaults();
    }

    private void makeDefaults() {

        if (this.title != null) {
            this.setDescription(this.title + "\n──────────────────────────────\n");
        }
        this.setThumbnail("https://crossoutdb.com/img/crossoutdb_logo_compact.png");
        this.setAuthor("Click here to invite the bot", XoMarketApplication.INVITE, jda.getSelfUser().getAvatarUrl());
        this.setFooter("Developed by Akio Nakao#0001");
    }

    @NotNull
    @Override
    public EmbedBuilder setFooter(@Nullable String text) {

        if (text == null) {
            super.setFooter("Powered by CrossoutDB");
        }
        return super.setFooter(String.format("%s • Powered by CrossoutDB", text));
    }

}
