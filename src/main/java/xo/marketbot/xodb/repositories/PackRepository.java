package xo.marketbot.xodb.repositories;

import fr.alexpado.lib.rest.interfaces.IRestAction;
import org.jetbrains.annotations.NotNull;
import xo.marketbot.entities.interfaces.crossout.RestRepository;
import xo.marketbot.entities.interfaces.game.IPack;
import xo.marketbot.xodb.XoDB;
import xo.marketbot.xodb.repositories.pack.FindAllPacksAction;

import java.util.List;
import java.util.Map;

public class PackRepository implements RestRepository<IPack, Integer> {

    private final XoDB xoDB;

    public PackRepository(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    /**
     * Retrieve one entity of the current type identifiable by its ID.
     *
     * @param integer
     *         The entity's identifier.
     *
     * @return A {@link IRestAction}
     */
    @Override
    public @NotNull IRestAction<IPack> findById(@NotNull Integer integer) {

        throw new UnsupportedOperationException("Finding a pack by its id isn't supported by CrossoutDB.");
    }

    /**
     * Retrieve every entities from the REST API of the current type.
     *
     * @return A {@link IRestAction}
     */
    @Override
    public IRestAction<List<IPack>> findAll() {

        return new FindAllPacksAction(this.xoDB);
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
    public IRestAction<List<IPack>> findAll(Map<String, Object> meta) {

        throw new UnsupportedOperationException("Searching for a pack isn't supported by CrossoutDB.");
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
    public IRestAction<List<IPack>> findAllByIds(Iterable<Integer> integers) {

        throw new UnsupportedOperationException("Finding packs by their its id isn't supported by CrossoutDB.");
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
    public IRestAction<List<IPack>> saveAll(Iterable<IPack> entities) {

        throw new UnsupportedOperationException("Saving packs isn't supported by CrossoutDB.");
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
    public IRestAction<IPack> save(IPack entity) {

        throw new UnsupportedOperationException("Saving a pack isn't supported by CrossoutDB.");
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
    public IRestAction<Void> delete(IPack entity) {

        throw new UnsupportedOperationException("Deleting a pack isn't supported by CrossoutDB.");
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
    public IRestAction<Void> deleteAll(Iterable<IPack> entities) {

        throw new UnsupportedOperationException("Deleting a pack isn't supported by CrossoutDB.");
    }
}
