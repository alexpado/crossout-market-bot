package fr.alexpado.bots.cmb.tools.embed;

import fr.alexpado.bots.cmb.tools.reaction.ReactionListener;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;

import java.util.ArrayList;
import java.util.List;

public abstract class EmbedPage<T> extends ReactionListener {

    private ArrayList<String> items = new ArrayList<>();
    private List<T> origin;
    private int currentPage = 1;
    private int totalPage = 1;
    private int count = 10;

    protected EmbedPage(Message message, List<T> items, int timeout) {
        super(message, timeout);
        this.origin = items;

        items.forEach(o -> this.items.add(asString(o)));
        this.currentPage = 1;

        float a = (float) items.size() / 10f;
        int b = items.size() / 10;

        if (a > b) {
            totalPage = b + 1;
        } else {
            totalPage = b;
        }
        totalPage = totalPage == 0 ? 1 : totalPage;

        if (items.size() > 10) {

            this.addAction("◀", reactionAction -> {
                previousPage();
                reactionAction.getReaction().removeReaction(reactionAction.getUser()).queue();
            });

            this.addAction("▶", reactionAction -> {
                nextPage();
                reactionAction.getReaction().removeReaction(reactionAction.getUser()).queue();
            });

            this.addAction("❌", reactionAction -> {
                timeout(message);
                reactionAction.getReaction().removeReaction(reactionAction.getUser()).queue();
            });
            refreshEmbed();
            start();
        } else {
            refreshEmbed();
        }
    }

    protected void reloadList() {
        this.items.clear();
        this.origin.forEach(o -> this.items.add(asString(o)));
    }

    public String asString(T obj) {
        return obj.toString();
    }

    public abstract EmbedBuilder getEmbed();

    protected void refreshEmbed() {
        this.resetTimer();
        this.getMessage().editMessage(this.getEmbed().setDescription(getPageText()).setFooter("Page " + currentPage + "/" + totalPage, null).build()).queue();
    }

    private boolean isPageValid() {
        int[] pageBound = this.getPageBound(this.items.size(), this.currentPage, this.count);
        return pageBound != null;
    }

    private String getPageText() {

        int[] pageBound = this.getPageBound(this.items.size(), this.currentPage, this.count);
        if (pageBound == null) return "";
        StringBuilder builder = new StringBuilder();
        for (int i = pageBound[0]; i < pageBound[1]; i++) {
            builder.append(this.items.get(i)).append("\n");
        }

        return builder.toString();
    }

    private void nextPage() {
        this.currentPage++;

        if (this.isPageValid()) {
            refreshEmbed();
        } else {
            this.currentPage--;
        }
    }

    private void previousPage() {
        this.currentPage--;

        if (this.isPageValid()) {
            refreshEmbed();
        } else {
            this.currentPage++;
        }
    }

    private int[] getPageBound(int size, int page, int count) {
        int startAt = (page - 1) * count;
        if (startAt > size || startAt < 0) return null;

        int endAt = startAt + count;
        if (endAt > size) endAt = size;
        return new int[]{startAt, endAt};
    }

}
