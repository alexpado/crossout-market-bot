package fr.alexpado.bots.cmb.xodb.repositories;

import fr.alexpado.bots.cmb.interfaces.crossout.RestRepository;
import fr.alexpado.bots.cmb.interfaces.game.IItem;
import fr.alexpado.bots.cmb.xodb.XoDB;
import fr.alexpado.bots.cmb.xodb.repositories.item.FindAllItemsAction;
import fr.alexpado.bots.cmb.xodb.repositories.item.FindItemByIdAction;
import fr.alexpado.lib.rest.interfaces.IRestAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ItemRepository implements RestRepository<IItem, Integer> {

    private final XoDB xoDB;

    public ItemRepository(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    /**
     * Retrieve one entity of the current type identifiable by its ID.
     *
     * @param id
     *         The entity's identifier.
     *
     * @return A {@link IRestAction}
     */
    @Override
    public @NotNull IRestAction<IItem> findById(@NotNull Integer id) {

        return new FindItemByIdAction(this.xoDB, id);
    }

    /**
     * Retrieve every entities from the REST API of the current type.
     *
     * @return A {@link IRestAction}
     */
    @Override
    public IRestAction<List<IItem>> findAll() {

        return new FindAllItemsAction(this.xoDB);
    }

    /**
     * Retrieve every entities of the current type matching the query represented by the provided map.
     *
     * @param meta
     *         A map of key-value that will be used to query the API.
     *
     * @return A {@link IRestAction}.
     */
    @Override
    public IRestAction<List<IItem>> findAll(Map<String, Object> meta) {

        return new FindAllItemsAction(this.xoDB, meta);
    }

    /**
     * Retrieve every entities of the current type having their identifier contained in the provided {@link Iterable}.
     *
     * @param integers
     *         A collection of ID identifying each entity to retrieve.
     *
     * @return A {@link IRestAction}
     */
    @Override
    public IRestAction<List<IItem>> findAllByIds(Iterable<Integer> integers) {

        throw new UnsupportedOperationException("Finding items by their ids isn't supported by CrossoutDB.");
    }

    /**
     * Save all provided entities of the current type. This may create entries if the identifier of an entity isn't set,
     * or update one if the identifier is defined.
     * <p>
     * If the identifier is set, but doesn't have any entries, it's up to the REST API implementation to refuse the
     * entity or to accept it as-is.
     *
     * @param entities
     *         A collection of entities to update or create.
     *
     * @return A {@link IRestAction}
     */
    @Override
    public IRestAction<List<IItem>> saveAll(Iterable<IItem> entities) {

        throw new UnsupportedOperationException("Saving items isn't supported by CrossoutDB.");
    }

    /**
     * Save the entity of the current type. This may create an entry if the provided entity's identifier isn't set or
     * update it if the identifier is defined.
     * <p>
     * If the identifier is set, but doesn't have any entry, it's up to the REST API implementation to refuse the entity
     * or to accept it as-is.
     *
     * @param entity
     *         An entity to update or create.
     *
     * @return A {@link IRestAction}
     */
    @Override
    public IRestAction<IItem> save(IItem entity) {

        throw new UnsupportedOperationException("Saving an item isn't supported by CrossoutDB.");
    }

    /**
     * Delete the provided entity. If the identifier isn't set, no request will be sent to the REST API and the promise
     * will fail instantly.
     *
     * @param entity
     *         An entity to remove.
     *
     * @return A {@link IRestAction}
     */
    @Override
    public IRestAction<Void> delete(IItem entity) {

        throw new UnsupportedOperationException("Deleting an item isn't supported by CrossoutDB.");
    }

    /**
     * Delete the provided entities. If one of the identifier isn't set, no request will be sent to the REST API and the
     * promise will fail instantly.
     *
     * @param entities
     *         A collection of entities to remove.
     *
     * @return A {@link IRestAction}
     */
    @Override
    public IRestAction<Void> deleteAll(Iterable<IItem> entities) {

        throw new UnsupportedOperationException("Deleting items isn't supported by CrossoutDB.");
    }
}
