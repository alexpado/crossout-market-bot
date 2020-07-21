package fr.alexpado.bots.cmb.cleaning.interfaces.crossout;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.*;
import fr.alexpado.bots.cmb.cleaning.interfaces.discord.IUserEntity;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IItem;

/**
 * Interface representing a watcher.
 *
 * @author alexpado
 */
public interface IWatcher extends Identifiable<Integer>, Nameable, Ownable<IUserEntity>, Marchantable, Updatable<Long>, Repeatable<Long>, Priceable {

    /**
     * Retrieve this {@link IWatcher}'s {@link IItem}'s id currently being watched.
     *
     * @return An {@link IItem}'s id.
     */
    Integer getItemId();

    /**
     * Define this {@link IWatcher}'s {@link IItem}'s id currently being watched.
     *
     * @param id
     *         An {@link IItem}'s id.
     */
    void setItemId(Integer id);

    /**
     * Retrieve this {@link IWatcher}'s {@link IItem}'s name currently being watched, in the {@link IUserEntity}'s
     * language.
     *
     * @return An {@link IItem}'s name.
     */
    String getItemName();

    /**
     * Define this {@link IWatcher}'s {@link IItem}'s name currently being watched, in the {@link IUserEntity}'s
     * language.
     *
     * @param name
     *         An {@link IItem}'s name.
     */
    void setItemName(String name);

    /**
     * Retrieve this {@link IWatcher}'s type.
     *
     * @return An integer
     */
    Integer getWatcherType();

    /**
     * Define this {@link IWatcher}'s type.
     *
     * @param watcherType
     *         An integer
     */
    void setWatcherType(Integer watcherType);

}
