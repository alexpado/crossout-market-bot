package xo.marketbot.commands.meta;

import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;
import xo.marketbot.library.services.translations.annotations.I18N;

public class ItemCommandMeta implements ICommandMeta {

    @I18N("command.item.description")
    private String description;

    @I18N("command.item.help")
    private String help;

    /**
     * Retrieves the label associated with an {@link ICommand}.
     *
     * @return A label.
     */
    @Override
    public String getLabel() {

        return "item";
    }

    /**
     * Retrieves the description associated with an {@link ICommand}. This will be used in the help menu.
     *
     * @return A description.
     */
    @Override
    public String getDescription() {

        return this.description;
    }

    /**
     * Retrieves the help associated with an {@link ICommand}. This will be used in the advanced help menu.
     *
     * @return A help.
     */
    @Override
    public String getHelp() {

        return this.help;
    }
}
