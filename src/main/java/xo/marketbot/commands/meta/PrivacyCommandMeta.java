package xo.marketbot.commands.meta;

import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;

public class PrivacyCommandMeta implements ICommandMeta {

    /**
     * Retrieves the label associated with an {@link ICommand}.
     *
     * @return A label.
     */
    @Override
    public String getLabel() {

        return "privacy";
    }

    /**
     * Retrieves the description associated with an {@link ICommand}. This will be used in the help menu.
     *
     * @return A description.
     */
    @Override
    public String getDescription() {

        return null;
    }

    /**
     * Retrieves the help associated with an {@link ICommand}. This will be used in the advanced help menu.
     *
     * @return A help.
     */
    @Override
    public String getHelp() {

        return null;
    }
}
