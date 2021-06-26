package xo.marketbot.library.interfaces;

/**
 * Interface representing an object aware of its {@link IDiscordBot} context.
 *
 * @author alexpado
 */
public interface IDiscordContext {

    /**
     * Get the {@link IDiscordBot} associated with this {@link IDiscordContext}.
     *
     * @return An {@link IDiscordBot} implementation instance.
     */
    IDiscordBot getBot();

}
