package xo.marketbot.xodb.repositories;

import fr.alexpado.lib.rest.interfaces.IRestAction;
import org.jetbrains.annotations.NotNull;
import xo.marketbot.entities.interfaces.crossout.RestRepository;
import xo.marketbot.entities.interfaces.game.IRarity;
import xo.marketbot.xodb.XoDB;
import xo.marketbot.xodb.repositories.rarity.FindAllRaritiesAction;

import java.util.List;
import java.util.Map;

public class RarityRepository implements RestRepository<IRarity, Integer> {

    private final XoDB xoDB;

    public RarityRepository(XoDB xoDB) {

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
    public @NotNull IRestAction<IRarity> findById(@NotNull Integer integer) {

        throw new UnsupportedOperationException("Finding a rarity by its id isn't supported by CrossoutDB.");
    }

    /**
     * Retrieve every entities from the REST API of the current type.
     *
     * @return A {@link IRestAction}
     */
    @Override
    public IRestAction<List<IRarity>> findAll() {

        return new FindAllRaritiesAction(this.xoDB);
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
    public IRestAction<List<IRarity>> findAll(Map<String, Object> meta) {

        throw new UnsupportedOperationException("Searching for rarities isn't supported by CrossoutDB.");
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
    public IRestAction<List<IRarity>> findAllByIds(Iterable<Integer> integers) {

        throw new UnsupportedOperationException("Finding rarities by their ids isn't supported by CrossoutDB.");
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
    public IRestAction<List<IRarity>> saveAll(Iterable<IRarity> entities) {

        throw new UnsupportedOperationException("Saving rarities isn't supported by CrossoutDB.");
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
    public IRestAction<IRarity> save(IRarity entity) {

        throw new UnsupportedOperationException("Saving a rarity isn't supported by CrossoutDB.");
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
    public IRestAction<Void> delete(IRarity entity) {

        throw new UnsupportedOperationException("Deleting a rarity isn't supported by CrossoutDB.");
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
    public IRestAction<Void> deleteAll(Iterable<IRarity> entities) {

        throw new UnsupportedOperationException("Deleting rarities isn't supported by CrossoutDB.");
    }
}
