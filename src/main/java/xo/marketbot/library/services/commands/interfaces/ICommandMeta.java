package xo.marketbot.library.services.commands.interfaces;

public interface ICommandMeta {

    /**
     * Retrieves the label associated with an {@link ICommand}.
     *
     * @return A label.
     */
    String getLabel();

    /**
     * Retrieves the description associated with an {@link ICommand}. This will be used in the help menu.
     *
     * @return A description.
     */
    String getDescription();

    /**
     * Retrieves the help associated with an {@link ICommand}. This will be used in the advanced help menu.
     *
     * @return A help.
     */
    String getHelp();

}
