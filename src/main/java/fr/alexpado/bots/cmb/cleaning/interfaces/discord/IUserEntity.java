package fr.alexpado.bots.cmb.cleaning.interfaces.discord;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Imageable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.LanguageHolder;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Nameable;
import fr.alexpado.bots.cmb.cleaning.interfaces.crossout.IWatcher;

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
