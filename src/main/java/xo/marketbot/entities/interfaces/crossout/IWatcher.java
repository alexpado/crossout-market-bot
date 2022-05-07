package xo.marketbot.entities.interfaces.crossout;

import fr.alexpado.xodb4j.interfaces.IItem;
import fr.alexpado.xodb4j.interfaces.common.Identifiable;
import fr.alexpado.xodb4j.interfaces.common.Marchantable;
import fr.alexpado.xodb4j.interfaces.common.Nameable;
import xo.marketbot.entities.discord.UserEntity;
import xo.marketbot.entities.interfaces.common.Fieldable;
import xo.marketbot.enums.WatcherTrigger;

import java.time.LocalDateTime;

public interface IWatcher extends Identifiable<Integer>, Nameable, Marchantable, Fieldable {

    /**
     * Retrieve this {@link IWatcher} trigger.
     *
     * @return A {@link WatcherTrigger}.
     */
    WatcherTrigger getTrigger();

    /**
     * Define this {@link IWatcher} trigger.
     *
     * @param trigger
     *         A {@link WatcherTrigger}.
     */
    void setTrigger(WatcherTrigger trigger);

    /**
     * Retrieve the {@link IItem}'s ID that this {@link IWatcher} is set to.
     *
     * @return An {@link IItem}'s ID.
     */
    int getItemId();

    /**
     * Define the {@link IItem}'s ID that this {@link IWatcher} is set to.
     *
     * @param id
     *         An {@link IItem}'s ID.
     */
    void setItemId(int id);

    /**
     * Retrieve the price limit which will trigger this {@link IWatcher} depending on its {@link #getTrigger()}. This
     * will most likely return null when {@link #getTrigger()} is set to {@link WatcherTrigger#EVERYTIME}.
     *
     * @return The price reference.
     */
    Double getPriceReference();

    /**
     * Define the price limit which will trigger this {@link IWatcher}. If {@link #getTrigger()} is
     * {@link WatcherTrigger#EVERYTIME}, this will most likely have any effect.
     *
     * @param price
     *         The price reference.
     */
    void setPriceReference(Double price);

    /**
     * Retrieve the {@link UserEntity} owner of this {@link IWatcher}.
     *
     * @return The {@link UserEntity}.
     */
    UserEntity getOwner();

    /**
     * Define the {@link UserEntity} owner of this {@link IWatcher}.
     *
     * @param owner
     *         The {@link UserEntity}.
     */
    void setOwner(UserEntity owner);

    /**
     * Check if this {@link IWatcher} will be run on a regular basis.
     *
     * @return True if regular, false otherwise.
     */
    boolean isRegular();

    /**
     * Define if this {@link IWatcher} should be run on a regular basis.
     *
     * @param regular
     *         True if regular, false otherwise.
     */
    void setRegular(boolean regular);

    /**
     * Retrieve the timing (interval) of this {@link IWatcher}.
     *
     * @return The timing (interval) in seconds.
     */
    long getTiming();

    /**
     * Define the timing (interval) of this {@link IWatcher}.
     *
     * @param timing
     *         The timing (interval) in seconds.
     */
    void setTiming(long timing);

    /**
     * Retrieve the last execution date of this {@link IWatcher}.
     *
     * @return The last execution date.
     */
    LocalDateTime getLastExecution();

    /**
     * Define the last execution date of this {@link IWatcher}.
     *
     * @param lastExecution
     *         The last execution date.
     */
    void setLastExecution(LocalDateTime lastExecution);

    /**
     * Retrieve the failure count encountered during this watcher execution.
     *
     * @return The failure count.
     */
    int getFailureCount();

    /**
     * Define this failure count encountered during this watcher execution.
     *
     * @param failureCount
     *         The failure count.
     */
    void setFailureCount(int failureCount);

}
