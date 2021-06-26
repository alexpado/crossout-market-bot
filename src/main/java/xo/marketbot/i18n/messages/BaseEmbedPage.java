package xo.marketbot.i18n.messages;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import xo.marketbot.XoMarketApplication;
import xo.marketbot.tools.embed.EmbedPage;

import java.util.List;

public abstract class BaseEmbedPage<T> extends EmbedPage<T> {

    public BaseEmbedPage(List<T> items) {

        super(items, 10);
    }

    @Override
    public EmbedBuilder getEmbed(JDA jda) {

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor(this.getInvite(), XoMarketApplication.INVITE, jda.getSelfUser().getAvatarUrl());
        builder.setDescription(this.getTitle() + "\n──────────────────────");

        return builder;
    }

    public abstract String getTitle();

    public abstract String getInvite();

}
