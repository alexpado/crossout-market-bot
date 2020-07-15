package fr.alexpado.bots.cmb.cleaning.xodb;

import fr.alexpado.bots.cmb.cleaning.interfaces.game.IItem;
import fr.alexpado.bots.cmb.cleaning.rest.interfaces.IRestRequest;
import fr.alexpado.bots.cmb.cleaning.rest.interfaces.RestRepository;
import fr.alexpado.bots.cmb.cleaning.rest.method.GetRestRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.util.List;
import java.util.Map;

public class ItemRepository implements RestRepository<IItem, Integer> {

    /**
     * Retrieve one entity of the current type identifiable by its ID.
     *
     * @param integer
     *         The entity's identifier.
     *
     * @return A {@link IRestRequest}
     */
    @Override
    public @NotNull IRestRequest<IItem> findById(@NotNull Integer integer) {

        return null;
    }

    /**
     * Retrieve every entities from the REST API of the current type.
     *
     * @return A {@link IRestRequest}
     */
    @Override
    public IRestRequest<List<IItem>> findAll() {

        return new GetRestRequest<List<IItem>>() {

            @Override
            public String getTargetUrl() {

                return "https://crossoutdb.com/api/v2/items";
            }

            @Override
            public List<IItem> convert(String output) {

                JSONArray jsonItems = new JSONArray(output);

                List<IItem>


                return null;
            }
        };
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
    public IRestRequest<List<IItem>> findAll(Map<String, Object> meta) {

        return null;
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
    public IRestRequest<List<IItem>> findAllByIds(Iterable<Integer> integers) {

        throw new UnsupportedOperationException("findAllByIds() isn't supported for this repository.");
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
    public IRestRequest<List<IItem>> saveAll(Iterable<IItem> entities) {

        throw new UnsupportedOperationException("saveAll() isn't allowed for this repository.");
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
    public IRestRequest<IItem> save(IItem entity) {

        throw new UnsupportedOperationException("save() isn't allowed for this repository.");
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
    public IRestRequest<Void> delete(IItem entity) {

        throw new UnsupportedOperationException("delete() isn't allowed for this repository.");
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
    public IRestRequest<Void> deleteAll(Iterable<IItem> entities) {

        throw new UnsupportedOperationException("deleteAll() isn't allowed for this repository.");
    }
}
