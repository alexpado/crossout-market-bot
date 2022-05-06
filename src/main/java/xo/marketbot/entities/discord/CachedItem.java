package xo.marketbot.entities.discord;

import org.jetbrains.annotations.NotNull;
import xo.marketbot.entities.interfaces.game.IItem;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class CachedItem implements Comparable<CachedItem> {

    @Id
    private int     id;
    private String  name;
    private boolean removed;
    private boolean meta;
    private String  rarity;
    private String  faction;
    private String  category;
    private String  type;

    public CachedItem() {}

    public CachedItem(IItem item) {

        item.setDupe(false);

        this.id       = item.getId();
        this.name     = item.getName();
        this.removed  = item.isRemoved();
        this.meta     = item.isMeta();
        this.rarity   = item.getRarity().getName();
        this.faction  = item.getFaction().getName();
        this.category = item.getCategory().getName();
        this.type     = item.getType().getName();
    }

    public int getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public boolean isRemoved() {

        return removed;
    }

    public boolean isMeta() {

        return meta;
    }

    public String getRarity() {

        return rarity;
    }

    public String getFaction() {

        return faction;
    }

    public String getCategory() {

        return category;
    }

    public String getType() {

        return type;
    }

    /**
     * Compares this object with the specified object for order.  Returns a negative integer, zero, or a positive
     * integer as this object is less than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure {@link Integer#signum
     * signum}{@code (x.compareTo(y)) == -signum(y.compareTo(x))} for all {@code x} and {@code y}.  (This implies that
     * {@code x.compareTo(y)} must throw an exception if and only if {@code y.compareTo(x)} throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * {@code (x.compareTo(y) > 0 && y.compareTo(z) > 0)} implies {@code x.compareTo(z) > 0}.
     *
     * <p>Finally, the implementor must ensure that {@code
     * x.compareTo(y)==0} implies that {@code signum(x.compareTo(z)) == signum(y.compareTo(z))}, for all {@code z}.
     *
     * @param o
     *         the object to be compared.
     *
     * @return a negative integer, zero, or a positive integer as this object is less than, equal to, or greater than
     *         the specified object.
     *
     * @throws NullPointerException
     *         if the specified object is null
     * @throws ClassCastException
     *         if the specified object's type prevents it from being compared to this object.
     */
    @Override
    public int compareTo(@NotNull CachedItem o) {

        return this.getName().compareTo(o.getName());
    }

    public String getDisplayName() {

        return String.format("%s (%s - %s - %s)", this.getName(), this.getFaction(), this.getCategory(), this.getRarity());
    }

}
