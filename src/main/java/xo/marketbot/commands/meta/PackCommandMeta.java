package xo.marketbot.commands.meta;

import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;
import xo.marketbot.library.services.translations.annotations.I18N;

public class PackCommandMeta implements ICommandMeta {

    @I18N("command.pack.description")
    private String description;

    @I18N("command.pack.help")
    private String help;

    /**
     * Retrieves the label associated with an {@link ICommand}.
     *
     * @return A label.
     */
    @Override
    public String getLabel() {

        return "pack";
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
