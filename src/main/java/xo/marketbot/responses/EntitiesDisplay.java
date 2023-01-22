package xo.marketbot.responses;

import fr.alexpado.jda.interactions.responses.ButtonResponse;
import fr.alexpado.jda.interactions.responses.SlashResponse;
import fr.alexpado.xodb4j.interfaces.common.Nameable;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.utils.messages.MessageRequest;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class EntitiesDisplay<T extends Nameable> implements SlashResponse, ButtonResponse {

    private static final int                              ITEM_PER_PAGE = 5;
    private final        List<T>                          items;
    private final        int                              itemPerPage;
    private final        int                              totalPage;
    private final        Supplier<? extends EmbedBuilder> renderer;
    private final        Function<T, MessageEmbed.Field>  fieldFunction;
    private              long                             transactionId;
    private              int                              page          = 1;

    public EntitiesDisplay(TranslationContext context, JDA jda, Function<T, MessageEmbed.Field> fieldFunction, List<T> items) {

        this(context, jda, fieldFunction, items, ITEM_PER_PAGE);
    }

    public EntitiesDisplay(TranslationContext context, JDA jda, Function<T, MessageEmbed.Field> fieldFunction, List<T> items, int itemPerPage) {

        this.fieldFunction = fieldFunction;

        this.items       = items;
        this.itemPerPage = itemPerPage;
        this.renderer    = () -> new DisplayTemplate(context, jda, context.getTranslation(TranslationService.TR_SEARCH__RESULTS));
        this.totalPage   = Math.floorDiv(items.size(), itemPerPage) + Math.min(1, items.size() % itemPerPage);

        this.items.sort(null);
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

    public void setTransactionId(long transactionId) {

        this.transactionId = transactionId;
    }

    public List<T> getRenderingItems() {

        int startIndex = (this.page - 1) * this.itemPerPage;
        int endIndex   = startIndex + this.itemPerPage;

        return this.items.subList(startIndex, Math.min(endIndex, this.items.size()));
    }

    @Override
    public Consumer<MessageRequest<?>> getHandler() {

        return (amb) -> {
            EmbedBuilder builder = this.render();
            ActionRow    buttons = this.getButtons();

            amb.setEmbeds(builder.build());
            amb.setComponents(buttons);
        };
    }

    @Override
    public boolean isEphemeral() {

        return false;
    }

    @Override
    public boolean shouldEditOriginalMessage() {

        return true;
    }

    private EmbedBuilder render() {

        EmbedBuilder builder = this.renderer.get();
        this.getRenderingItems().stream().map(this.fieldFunction).forEach(builder::addField);
        return builder;
    }

    private ActionRow getButtons() {

        Button previous = this.createButton("previous", "❰");
        Button page = Button.secondary("pagination://ignore", "%s / %s".formatted(this.page, this.totalPage))
                            .asDisabled();
        Button next = this.createButton("next", "❱");

        if (!this.isPreviousPageAvailable()) {
            previous = previous.asDisabled();
        }

        if (!this.isNextPageAvailable()) {
            next = next.asDisabled();
        }

        return ActionRow.of(previous, page, next);
    }

    private Button createButton(String action, String label) {

        return Button.primary("pagination://%s?action=%s".formatted(this.transactionId, action), label);
    }

}
