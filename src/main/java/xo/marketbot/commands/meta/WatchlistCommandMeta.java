package xo.marketbot.commands.meta;

import xo.marketbot.library.services.commands.interfaces.ICommand;
import xo.marketbot.library.services.commands.interfaces.ICommandMeta;
import xo.marketbot.library.services.translations.annotations.I18N;

public class WatchlistCommandMeta implements ICommandMeta {

    @I18N("command.watchlist.description")
    private String description;

    @I18N("command.watchlist.help")
    private String help;

    /**
     * Retrieves the label associated with an {@link ICommand}.
     *
     * @return A label.
     */
    @Override
    public String getLabel() {

        return "watchlist";
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
