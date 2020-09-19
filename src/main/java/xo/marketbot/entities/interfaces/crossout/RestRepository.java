package xo.marketbot.entities.interfaces.crossout;

import fr.alexpado.lib.rest.interfaces.IRestAction;
import org.jetbrains.annotations.NotNull;
import xo.marketbot.entities.interfaces.common.Identifiable;

import java.util.List;
import java.util.Map;

/**
 * Interface representing a REST Repository, allowing to request a remote REST API to retrieve entities.
 * <p>
 * The targeted entity must implement the interface {@link Identifiable} to ensure that every entity can be obtained
 * separately.
 * <p>
 * Every requests may fail if two or more entity share the same identifier, meaning that serious bug could occur in any
 * program using the API and requiring identifiable entities.
 *
 * @param <T>
 *         Type of the entity that will be retrieved from the REST API
 * @param <ID>
 *         Type of the entity type's identifier.
 *
 * @author alexpado
 */
public interface RestRepository<T extends Identifiable<ID>, ID> {

    /**
     * Retrieve one entity of the current type identifiable by its ID.
     *
     * @param id
     *         The entity's identifier.
     *
     * @return A {@link IRestAction}
     */
    @NotNull IRestAction<T> findById(@NotNull ID id);

    /**
     * Retrieve every entities from the REST API of the current type.
     *
     * @return A {@link IRestAction}
     */
    IRestAction<List<T>> findAll();

    /**
     * Retrieve every entities of the current type matching the query represented by the provided map.
     *
     * @param meta
     *         A map of key-value that will be used to query the API.
     *
     * @return A {@link IRestAction}.
     */
    IRestAction<List<T>> findAll(Map<String, Object> meta);

    /**
     * Retrieve every entities of the current type having their identifier contained in the provided {@link Iterable}.
     *
     * @param ids
     *         A collection of ID identifying each entity to retrieve.
     *
     * @return A {@link IRestAction}
     */
    IRestAction<List<T>> findAllByIds(Iterable<ID> ids);

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
    IRestAction<List<T>> saveAll(Iterable<T> entities);

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
    IRestAction<T> save(T entity);

    /**
     * Delete the provided entity. If the identifier isn't set, no request will be sent to the REST API and the promise
     * will fail instantly.
     *
     * @param entity
     *         An entity to remove.
     *
     * @return A {@link IRestAction}
     */
    IRestAction<Void> delete(T entity);

    /**
     * Delete the provided entities. If one of the identifier isn't set, no request will be sent to the REST API and the
     * promise will fail instantly.
     *
     * @param entities
     *         A collection of entities to remove.
     *
     * @return A {@link IRestAction}
     */
    IRestAction<Void> deleteAll(Iterable<T> entities);

}
