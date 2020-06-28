package fr.alexpado.bots.cmb.tools.embed;

import fr.alexpado.bots.cmb.tools.reaction.ReactionListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class EmbedPage<T> extends ReactionListener {

    private final ArrayList<String> items       = new ArrayList<>();
    private final List<T>           origin;
    private final int               count       = 10;
    private       int               currentPage = 1;
    private       int               totalPage   = 1;

    protected EmbedPage(Message message, List<T> items, int timeout) {

        super(message, timeout);
        this.origin = items;

        items.forEach(o -> this.items.add(this.asString(o)));
        this.currentPage = 1;

        float a = (float) items.size() / 10f;
        int   b = items.size() / 10;

        if (a > b) {
            this.totalPage = b + 1;
        } else {
            this.totalPage = b;
        }
        this.totalPage = this.totalPage == 0 ? 1 : this.totalPage;

        if (items.size() > 10) {

            this.addAction("◀", reactionAction -> {
                this.previousPage();
                reactionAction.getReaction().removeReaction(reactionAction.getUser()).queue();
            });

            this.addAction("▶", reactionAction -> {
                this.nextPage();
                reactionAction.getReaction().removeReaction(reactionAction.getUser()).queue();
            });

            this.addAction("❌", reactionAction -> {
                this.timeout(message);
                reactionAction.getReaction().removeReaction(reactionAction.getUser()).queue();
            });
            this.refreshEmbed();
            this.start();
        } else {
            this.refreshEmbed();
        }
    }

    protected void reloadList() {

        this.items.clear();
        this.origin.forEach(o -> this.items.add(this.asString(o)));
    }

    public String asString(T obj) {

        return obj.toString();
    }

    public abstract EmbedBuilder getEmbed();

    protected void refreshEmbed() {

        this.resetTimer();
        this.getMessage()
            .editMessage(this.getEmbed()
                             .setDescription(this.getPageText())
                             .setFooter("Page " + this.currentPage + "/" + this.totalPage, null)
                             .build())
            .queue();
    }

    private boolean isPageValid() {

        int[] pageBound = this.getPageBound(this.items.size(), this.currentPage, this.count);
        return pageBound != null;
    }

    private String getPageText() {

        int[] pageBound = this.getPageBound(this.items.size(), this.currentPage, this.count);
        if (pageBound == null) { return ""; }
        StringBuilder builder = new StringBuilder();
        for (int i = pageBound[0] ; i < pageBound[1] ; i++) {
            builder.append(this.items.get(i)).append("\n");
        }

        return builder.toString();
    }

    private void nextPage() {

        this.currentPage++;

        if (this.isPageValid()) {
            this.refreshEmbed();
        } else {
            this.currentPage--;
        }
    }

    private void previousPage() {

        this.currentPage--;

        if (this.isPageValid()) {
            this.refreshEmbed();
        } else {
            this.currentPage++;
        }
    }

    private int[] getPageBound(int size, int page, int count) {

        int startAt = (page - 1) * count;
        if (startAt > size || startAt < 0) { return null; }

        int endAt = startAt + count;
        if (endAt > size) { endAt = size; }
        return new int[]{startAt, endAt};
    }

}
