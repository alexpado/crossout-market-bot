package xo.marketbot.responses;

import fr.alexpado.jda.interactions.entities.responses.PaginatedResponse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.interactions.components.Component;
import xo.marketbot.entities.interfaces.common.Fieldable;
import xo.marketbot.entities.interfaces.common.InteractionTarget;
import xo.marketbot.entities.interfaces.common.Nameable;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.services.i18n.TranslationService;

import java.util.ArrayList;
import java.util.List;

public class EntitiesDisplay<T extends Fieldable & Nameable> extends PaginatedResponse<T> {

    public EntitiesDisplay(TranslationContext context, JDA jda, List<T> items) {

        super(
                () -> new DisplayTemplate(context, jda, context.getTranslation(TranslationService.TR_SEARCH__RESULTS)),
                items,
                5
        );
    }

    @Override
    public void render(EmbedBuilder embed, List<T> items) {

        items.stream().map(Fieldable::toField).forEach(embed::addField);
    }

    @Override
    public ActionRow[] getActionRows(String id) {

        List<Component> components = new ArrayList<>();

        for (T pageItem : this.getPageItems()) {
            if (pageItem instanceof InteractionTarget target) {
                components.add(Button.of(ButtonStyle.SECONDARY, target.getInteractionPath()
                        .toString(), pageItem.getName()));
            }
        }

        if (components.isEmpty()) {
            return new ActionRow[]{
                    ActionRow.of(this.getPreviousButton(id), this.getNextButton(id))
            };
        }
        return new ActionRow[]{
                ActionRow.of(this.getPreviousButton(id), this.getNextButton(id)),
                ActionRow.of(components.toArray(Component[]::new))
        };
    }

}
