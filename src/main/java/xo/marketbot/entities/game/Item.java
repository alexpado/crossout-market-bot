package xo.marketbot.entities.game;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONObject;
import xo.marketbot.entities.interfaces.common.*;
import xo.marketbot.entities.interfaces.game.*;
import xo.marketbot.tools.Utilities;
import xo.marketbot.xodb.XoDB;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * Class implementing the {@link IItem} interface.
 * <p>
 * All fields are read-only because these data aren't meant to be edited, as they come from the CrossoutDB's API.
 *
 * @author alexpado
 */
public class Item implements IItem {

    private final int           id;
    private final String        availableName;
    private final String        description;
    private final boolean       removed;
    private final boolean       meta;
    private final boolean       craftable;
    private final int           marketBuy;
    private final int           marketSell;
    private final int           craftingSellSum;
    private final int           craftingBuySum;
    private final LocalDateTime lastUpdate;
    private final IRarity       rarity;
    private final IType         type;
    private final ICategory     category;
    private final IFaction      faction;

    private boolean dupe;

    /**
     * Create a new {@link Item} instance.
     *
     * @param xoDB
     *         The {@link XoDB} instance to use when retrieving specific data about this {@link Item} sub-properties.
     * @param source
     *         JSON containing all values needed to create this {@link Item}.
     */
    public Item(XoDB xoDB, JSONObject source) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        this.id              = source.getInt("id");
        this.availableName   = source.getString("availableName");
        this.description     = Utilities.removeHTML(source.get("description") == JSONObject.NULL ? "" : source.getString("description"));
        this.marketBuy       = source.getInt("buyPrice");
        this.marketSell      = source.getInt("sellPrice");
        this.craftingSellSum = source.getInt("craftingSellSum");
        this.craftingBuySum  = source.getInt("craftingBuySum");
        this.removed         = source.getInt("removed") == 1;
        this.meta            = source.getInt("meta") == 1;
        this.craftable       = source.getInt("craftable") == 1;
        this.lastUpdate      = LocalDateTime.parse(source.getString("lastUpdateTime"), formatter);

        this.rarity   = xoDB.fromRarityCache(source.getInt("rarityId"));
        this.type     = xoDB.fromTypeCache(source.getInt("typeId"));
        this.category = xoDB.fromCategoryCache(source.getInt("categoryId"));
        this.faction  = xoDB.fromFactionCache(source.getInt("factionNumber"));

        this.dupe = false;
    }

    /**
     * Retrieve the link to use to redirect to the CrossoutDB page of the {@link IItem} identified by the provided id.
     *
     * @param id
     *         The {@link IItem}'s id
     *
     * @return An URL
     */
    public static String toXoDBLink(int id) {

        return String.format("https://crossoutdb.com/item/%s?ref=crossoutmarketbot", id);
    }

    /**
     * Retrieve the link retrieve the thumbnail of the {@link IItem} identified by the provided id.
     *
     * @param id
     *         The {@link IItem}'s id
     *
     * @return An URL
     */
    public static String toXoDBThumbnail(int id, LocalDateTime updated) {

        return String.format("https://crossoutdb.com/img/items/%s.png?ref=crossoutmarketbot&time=%s", id, updated.toEpochSecond(ZoneOffset.UTC));
    }

    /**
     * Checks this {@link Craftable}'s craftable state.
     *
     * @return True if craftable, false instead.
     */
    @Override
    public boolean isCraftable() {

        return this.craftable;
    }

    /**
     * Retrieve the amount of money needed to buy every {@link IItem} of this {@link Craftable}'s crafting recipe. The
     * value returned by this method should be ignored if {@link #isCraftable()} returns false.
     *
     * @return Amount of money needed to craft.
     */
    @Override
    public double getBuyCraftPrice() {

        return this.craftingBuySum;
    }

    /**
     * Retrieve the amount of money obtainable by selling every {@link IItem} of this {@link Craftable}'s crafting
     * recipe. The value returned by this method should be ignored if {@link #isCraftable()} returns false.
     *
     * @return Amount of money obtainable.
     */
    @Override
    public double getSellCraftPrice() {

        return this.craftingSellSum;
    }

    /**
     * Retrieve this {@link Describable}'s description.
     *
     * @return The description.
     */
    @Override
    public String getDescription() {

        return this.description;
    }

    /**
     * Retrieve this {@link Identifiable}'s ID.
     *
     * @return An ID.
     */
    @Override
    public Integer getId() {

        return this.id;
    }

    /**
     * Retrieve the amount of money needed to buy this {@link Marchantable}.
     *
     * @return The buy price
     */
    @Override
    public double getMarketSell() {

        return this.marketSell;
    }

    /**
     * Retrieve the amount of money obtainable by selling this {@link Marchantable}.
     *
     * @return The sell price
     */
    @Override
    public double getMarketBuy() {

        return this.marketBuy;
    }

    /**
     * Retrieve this {@link Nameable}'s name.
     *
     * @return The name.
     */
    @Override
    public String getName() {

        if (this.dupe) {
            return String.format("%s (%s)", this.availableName, this.getRarity().getName());
        }
        return this.availableName;
    }

    /**
     * Retrieve this {@link Updatable}'s last update time.
     *
     * @return The time
     */
    @Override
    public LocalDateTime getLastUpdate() {

        return this.lastUpdate;
    }

    /**
     * Check if this {@link IItem} has been removed from the game.
     *
     * @return True if removed, false instead.
     */
    @Override
    public boolean isRemoved() {

        return this.removed;
    }

    /**
     * Check if this {@link IItem} is a meta item.
     *
     * @return True if it's a meta item.
     */
    @Override
    public boolean isMeta() {

        return this.meta;
    }

    /**
     * Get the {@link IType} of this {@link IItem}.
     *
     * @return An {@link IType} instance.
     */
    @Override
    public IType getType() {

        return this.type;
    }

    /**
     * Get the {@link ICategory} of this {@link IItem}.
     *
     * @return An {@link ICategory} instance.
     */
    @Override
    public ICategory getCategory() {

        return this.category;
    }

    /**
     * Get the {@link IRarity} of this {@link IItem}.
     *
     * @return An {@link IRarity} instance.
     */
    @Override
    public IRarity getRarity() {

        return this.rarity;
    }

    /**
     * Get the {@link IFaction} of this {@link IItem}.
     *
     * @return An {@link IFaction} instance.
     */
    @Override
    public IFaction getFaction() {

        return this.faction;
    }

    /**
     * Mark this {@link IItem} as duplicate in its context. This will change the {@link #getName()} behavior by
     * appending the rarity name to the {@link IItem} name.
     *
     * @param dupe
     *         True to mark this {@link IItem} as duplicated.
     */
    @Override
    public void setDupe(boolean dupe) {

        this.dupe = dupe;
    }

    @Override
    public MessageEmbed.Field toField() {

        String factionName = this.getFaction().getName();
        String rarityName  = this.getRarity().getName();

        return new MessageEmbed.Field(this.getName(), String.format("%s • %s", factionName, rarityName), false);
    }

}
