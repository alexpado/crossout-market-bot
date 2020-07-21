package fr.alexpado.bots.cmb.cleaning;

import fr.alexpado.bots.cmb.cleaning.interfaces.game.*;
import fr.alexpado.bots.cmb.cleaning.rest.interfaces.RestRepository;
import fr.alexpado.bots.cmb.cleaning.xodb.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class XoDB {

    private final static Logger LOGGER = LoggerFactory.getLogger(XoDB.class);

    private final String rootUrl;

    private final Map<Integer, IRarity>   rarityCache   = new HashMap<>();
    private final Map<Integer, IFaction>  factionCache  = new HashMap<>();
    private final Map<Integer, IType>     typeCache     = new HashMap<>();
    private final Map<Integer, ICategory> categoryCache = new HashMap<>();

    /**
     * Create a new instance of {@link XoDB}.
     *
     * @param rootUrl
     *         The root url common to all endpoints.
     */
    public XoDB(String rootUrl) {

        this.rootUrl = rootUrl;
        this.buildCaches();
    }

    /**
     * Retrieve the root url common to all endpoints.
     *
     * @return A string
     */
    public String getRootUrl() {

        return rootUrl;
    }

    /**
     * Fill the cache with almost constant value from the API. Calling this method more than once will just rebuild the
     * cache.
     */
    public void buildCaches() {

        LOGGER.info("Clearing entity cache...");

        this.rarityCache.clear();
        this.factionCache.clear();
        this.typeCache.clear();
        this.categoryCache.clear();

        LOGGER.info("Building cache...");

        try {
            LOGGER.debug("Building rarities cache...");
            this.rarities().findAll().complete().forEach(rarity -> this.rarityCache.put(rarity.getId(), rarity));

            LOGGER.debug("Building factions cache...");
            this.factions().findAll().complete().forEach(faction -> this.factionCache.put(faction.getId(), faction));

            LOGGER.debug("Building types cache...");
            this.types().findAll().complete().forEach(type -> this.typeCache.put(type.getId(), type));

            LOGGER.debug("Building categories cache...");
            this.categories()
                .findAll()
                .complete()
                .forEach(category -> this.categoryCache.put(category.getId(), category));

            LOGGER.info("Cache built.");
        } catch (IOException e) {
            LOGGER.error("An error occurred while building the cache. The cache may not be complete.", e);
        }
    }

    /**
     * Retrieve the {@link RestRepository} allowing to query CrossoutDB for retrieving {@link IRarity}.
     *
     * @return A {@link RestRepository} instance.
     */
    public RestRepository<IRarity, Integer> rarities() {

        return new RarityRepository(this);
    }

    /**
     * Retrieve the {@link RestRepository} allowing to query CrossoutDB for retrieving {@link IItem}.
     *
     * @return A {@link RestRepository} instance.
     */
    public RestRepository<IItem, Integer> items() {

        return new ItemRepository(this);
    }

    /**
     * Retrieve the {@link RestRepository} allowing to query CrossoutDB for retrieving {@link IFaction}.
     *
     * @return A {@link RestRepository} instance.
     */
    public RestRepository<IFaction, Integer> factions() {

        return new FactionRepository(this);
    }

    /**
     * Retrieve the {@link RestRepository} allowing to query CrossoutDB for retrieving {@link IType}.
     *
     * @return A {@link RestRepository} instance.
     */
    public RestRepository<IType, Integer> types() {

        return new TypeRepository(this);
    }

    /**
     * Retrieve the {@link RestRepository} allowing to query CrossoutDB for retrieving {@link ICategory}.
     *
     * @return A {@link RestRepository} instance.
     */
    public RestRepository<ICategory, Integer> categories() {

        return new CategoryRepository(this);
    }

    /**
     * Retrieve the {@link RestRepository} allowing to query CrossoutDB for retrieving {@link IPack}.
     *
     * @return A {@link RestRepository} instance.
     */
    public RestRepository<IPack, Integer> packs() {

        return new PackRepository(this);
    }

    /**
     * Retrieve an {@link IRarity} from the cache for the provided id.
     *
     * @param id
     *         The id of the {@link IRarity}.
     *
     * @return The {@link IRarity} matching the id, or a default instance (id = 0) if not found.
     */
    public IRarity fromRarityCache(int id) {

        return this.rarityCache.getOrDefault(id, new IRarity() {

            @Override
            public Color getColor() {

                return Color.BLACK;
            }

            @Override
            public Integer getId() {

                return 0;
            }

            @Override
            public String getName() {

                return "No Rarity";
            }
        });
    }

    /**
     * Retrieve an {@link IType} from the cache for the provided id.
     *
     * @param id
     *         The id of the {@link IType}.
     *
     * @return The {@link IType} matching the id, or a default instance (id = 0) if not found.
     */
    public IType fromTypeCache(int id) {

        return this.typeCache.getOrDefault(id, new IType() {

            @Override
            public Integer getId() {

                return 0;
            }

            @Override
            public String getName() {

                return "No Type";
            }
        });
    }

    /**
     * Retrieve an {@link IFaction} from the cache for the provided id.
     *
     * @param id
     *         The id of the {@link IFaction}.
     *
     * @return The {@link IFaction} matching the id, or a default instance (id = 0) if not found.
     */
    public IFaction fromFactionCache(int id) {

        return this.factionCache.getOrDefault(id, new IFaction() {

            @Override
            public Integer getId() {

                return 0;
            }

            @Override
            public String getName() {

                return "No Faction";
            }
        });
    }

    /**
     * Retrieve an {@link ICategory} from the cache for the provided id.
     *
     * @param id
     *         The id of the {@link ICategory}.
     *
     * @return The {@link ICategory} matching the id, or a default instance (id = 0) if not found.
     */
    public ICategory fromCategoryCache(int id) {

        return this.categoryCache.getOrDefault(id, new ICategory() {

            @Override
            public Integer getId() {

                return 0;
            }

            @Override
            public String getName() {

                return "No Category";
            }
        });
    }
}