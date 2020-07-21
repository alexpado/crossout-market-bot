package fr.alexpado.bots.cmb.cleaning.xodb;

import fr.alexpado.bots.cmb.cleaning.XoDB;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.ICategory;
import fr.alexpado.bots.cmb.cleaning.rest.interfaces.IRestRequest;
import fr.alexpado.bots.cmb.cleaning.rest.interfaces.RestRepository;
import fr.alexpado.bots.cmb.cleaning.xodb.category.FindAllCategoriesAction;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class CategoryRepository implements RestRepository<ICategory, Integer> {

    private final XoDB xoDB;

    public CategoryRepository(XoDB xoDB) {

        this.xoDB = xoDB;
    }

    /**
     * Retrieve one entity of the current type identifiable by its ID.
     *
     * @param integer
     *         The entity's identifier.
     *
     * @return A {@link IRestRequest}
     */
    @Override
    public @NotNull IRestRequest<ICategory> findById(@NotNull Integer integer) {

        throw new UnsupportedOperationException("Finding a category by its id isn't supported by CrossoutDB.");
    }

    /**
     * Retrieve every entities from the REST API of the current type.
     *
     * @return A {@link IRestRequest}
     */
    @Override
    public IRestRequest<List<ICategory>> findAll() {

        return new FindAllCategoriesAction(this.xoDB);
    }

    /**
     * Retrieve every entities of the current type matching the query represented by the provided map.
     *
     * @param meta
     *         A map of key-value that will be used to query the API.
     *
     * @return A {@link IRestRequest}.
     */
    @Override
    public IRestRequest<List<ICategory>> findAll(Map<String, Object> meta) {

        throw new UnsupportedOperationException("Searching categories isn't supported by CrossoutDB.");
    }

    /**
     * Retrieve every entities of the current type having their identifier contained in the provided {@link Iterable}.
     *
     * @param integers
     *         A collection of ID identifying each entity to retrieve.
     *
     * @return A {@link IRestRequest}
     */
    @Override
    public IRestRequest<List<ICategory>> findAllByIds(Iterable<Integer> integers) {

        throw new UnsupportedOperationException("Finding categories by their ids isn't supported by CrossoutDB.");
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
     * @return A {@link IRestRequest}
     */
    @Override
    public IRestRequest<List<ICategory>> saveAll(Iterable<ICategory> entities) {

        throw new UnsupportedOperationException("Saving categories isn't supported by CrossoutDB.");
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
     * @return A {@link IRestRequest}
     */
    @Override
    public IRestRequest<ICategory> save(ICategory entity) {

        throw new UnsupportedOperationException("Saving categories isn't supported by CrossoutDB.");
    }

    /**
     * Delete the provided entity. If the identifier isn't set, no request will be sent to the REST API and the promise
     * will fail instantly.
     *
     * @param entity
     *         An entity to remove.
     *
     * @return A {@link IRestRequest}
     */
    @Override
    public IRestRequest<Void> delete(ICategory entity) {

        throw new UnsupportedOperationException("Deleting a category isn't supported by CrossoutDB.");
    }

    /**
     * Delete the provided entities. If one of the identifier isn't set, no request will be sent to the REST API and the
     * promise will fail instantly.
     *
     * @param entities
     *         A collection of entities to remove.
     *
     * @return A {@link IRestRequest}
     */
    @Override
    public IRestRequest<Void> deleteAll(Iterable<ICategory> entities) {

        throw new UnsupportedOperationException("Deleting categories isn't supported by CrossoutDB.");
    }
}
