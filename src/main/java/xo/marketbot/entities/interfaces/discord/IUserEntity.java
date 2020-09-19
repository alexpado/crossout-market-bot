package xo.marketbot.entities.interfaces.discord;

import xo.marketbot.entities.interfaces.common.Identifiable;
import xo.marketbot.entities.interfaces.common.Imageable;
import xo.marketbot.entities.interfaces.common.LanguageHolder;
import xo.marketbot.entities.interfaces.common.Nameable;
import xo.marketbot.entities.interfaces.crossout.IWatcher;

/**
 * Interface representing a custom discord user.
 *
 * @author alexpado
 */
public interface IUserEntity extends Identifiable<Long>, Nameable, Imageable, LanguageHolder {

    /**
     * Retrieve this {@link IUserEntity}'s discriminator.
     *
     * @return A String.
     */
    String getDiscriminator();

    /**
     * Define this {@link IUserEntity}'s discriminator.
     *
     * @param discriminator
     *         The {@link IUserEntity}'s discriminator
     */
    void setDiscriminator(String discriminator);

    /**
     * Check if no {@link IWatcher}s should be sent to this {@link IUserEntity}.
     *
     * @return True if no {@link IWatcher} should be sent, false instead.
     */
    boolean isWatchersPaused();

    /**
     * Define if no {@link IWatcher} should be sent to this {@link IUserEntity}.
     *
     * @param watchersPaused
     *         True if no {@link IWatcher} should be sent, false instead.
     */
    void setWatchersPaused(boolean watchersPaused);

}
