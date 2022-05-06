package xo.marketbot.responses;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import xo.marketbot.entities.interfaces.common.Fieldable;
import xo.marketbot.entities.interfaces.common.Nameable;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;

import java.util.List;
import java.util.function.Supplier;

public class EntitiesDisplay<T extends Fieldable & Nameable> {

    private static final int ITEM_PER_PAGE = 5;

    private final List<T>                          items;
    private final int                              itemPerPage;
    private final int                              totalPage;
    private final Supplier<? extends EmbedBuilder> renderer;
    private       int                              page = 1;

    public EntitiesDisplay(TranslationContext context, JDA jda, List<T> items) {

        this(context, jda, items, ITEM_PER_PAGE);
    }

    public EntitiesDisplay(TranslationContext context, JDA jda, List<T> items, int itemPerPage) {

        this.items       = items;
        this.itemPerPage = itemPerPage;
        this.renderer    = () -> new DisplayTemplate(context, jda, context.getTranslation(TranslationService.TR_SEARCH__RESULTS));
        this.totalPage   = Math.floorDiv(items.size(), itemPerPage) + Math.min(1, items.size() % itemPerPage);
    }

    public boolean isNextPageAvailable() {

        return this.page < this.totalPage;
    }

    public boolean isPreviousPageAvailable() {

        return this.page > 1;
    }

    public void next() {

        if (this.isNextPageAvailable()) {
            this.page++;
        }
    }

    public void previous() {

        if (this.isPreviousPageAvailable()) {
            this.page--;
        }
    }

    public List<T> getRenderingItems() {

        int startIndex = (this.page - 1) * this.itemPerPage;
        int endIndex   = startIndex + this.itemPerPage;

        return this.items.subList(startIndex, Math.min(endIndex, this.items.size()));
    }

    public EmbedBuilder render() {

        EmbedBuilder builder = this.renderer.get();
        this.getRenderingItems().stream().map(Fieldable::toField).forEach(builder::addField);
        return builder;
    }

    private Button createButton(String action, String id, String label) {

        return Button.primary("pagination://%s?action=%s".formatted(id, action), label);
    }

    public ActionRow[] getActionRows(String id) {

        Button previous = this.createButton("previous", id, "❰");
        Button page = Button.secondary("pagination://ignore", "%s / %s".formatted(this.page, this.totalPage))
                .asDisabled();
        Button next = this.createButton("next", id, "❱");

        if (!this.isPreviousPageAvailable()) {
            previous = previous.asDisabled();
        }

        if (!this.isNextPageAvailable()) {
            next = next.asDisabled();
        }

        return new ActionRow[]{
                ActionRow.of(previous, page, next),
        };
    }

}
