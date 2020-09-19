package xo.marketbot.entities.discord;

import org.jetbrains.annotations.NotNull;
import xo.marketbot.entities.interfaces.common.*;
import xo.marketbot.entities.interfaces.crossout.IWatcher;
import xo.marketbot.entities.interfaces.discord.IUserEntity;
import xo.marketbot.entities.interfaces.game.IItem;

import javax.persistence.*;

@Entity
public class Watcher implements IWatcher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @OneToOne
    @NotNull
    private User user;

    private Integer itemId;

    private String itemName;

    private Double sellPrice;

    private Double buyPrice;

    private Integer watcherType;

    private Double price;

    private Long repeatCron;


    public static Watcher create(User user, IItem item) {

        Watcher watcher = new Watcher();

        watcher.setOwner(user);
        watcher.setItemId(item.getId());
        watcher.setItemName(item.getName());
        watcher.setSellPrice(item.getSellPrice());
        watcher.setBuyPrice(item.getBuyPrice());

        return watcher;
    }


    /**
     * Retrieve this {@link IWatcher}'s {@link IItem}'s id currently being watched.
     *
     * @return An {@link IItem}'s id.
     */
    @Override
    public Integer getItemId() {

        return this.itemId;
    }

    /**
     * Define this {@link IWatcher}'s {@link IItem}'s id currently being watched.
     *
     * @param id
     *         An {@link IItem}'s id.
     */
    @Override
    public void setItemId(Integer id) {

        this.itemId = id;
    }

    /**
     * Retrieve this {@link IWatcher}'s {@link IItem}'s name currently being watched, in the {@link IUserEntity}'s
     * language.
     *
     * @return An {@link IItem}'s name.
     */
    @Override
    public String getItemName() {

        return this.itemName;
    }

    /**
     * Define this {@link IWatcher}'s {@link IItem}'s name currently being watched, in the {@link IUserEntity}'s
     * language.
     *
     * @param name
     *         An {@link IItem}'s name.
     */
    @Override
    public void setItemName(String name) {

        this.itemName = name;
    }

    /**
     * Retrieve this {@link IWatcher}'s type.
     *
     * @return An integer
     */
    @Override
    public Integer getWatcherType() {

        return this.watcherType;
    }

    /**
     * Define this {@link IWatcher}'s type.
     *
     * @param watcherType
     *         An integer
     */
    @Override
    public void setWatcherType(Integer watcherType) {

        this.watcherType = watcherType;
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
    public double getBuyPrice() {

        return this.buyPrice;
    }

    /**
     * Define the amount of money needed to buy this {@link Marchantable}.
     *
     * @param price
     *         The buy price.
     */
    @Override
    public void setBuyPrice(double price) {

        this.buyPrice = price;
    }

    /**
     * Retrieve the amount of money obtainable by selling this {@link Marchantable}.
     *
     * @return The sell price
     */
    @Override
    public double getSellPrice() {

        return this.sellPrice;
    }

    /**
     * Define the amount of money obtainable by selling this {@link Marchantable}
     *
     * @param price
     *         The sell price
     */
    @Override
    public void setSellPrice(double price) {

        this.sellPrice = price;
    }

    /**
     * Retrieve this {@link Nameable}'s name.
     *
     * @return The name.
     */
    @Override
    public String getName() {

        return this.name;
    }

    /**
     * Define this {@link Nameable}'s name.
     *
     * @param name
     *         The name.
     */
    @Override
    public void setName(String name) {

        this.name = name;
    }

    /**
     * Retrieve this {@link Ownable}'s owner.
     *
     * @return The owner.
     */
    @Override
    public IUserEntity getOwner() {

        return this.user;
    }

    /**
     * Define this {@link Ownable}'s owner.
     *
     * @param owner
     *         The owner.
     */
    @Override
    public void setOwner(IUserEntity owner) {

        if (owner instanceof User) {
            this.user = ((User) owner);
        }
        throw new IllegalArgumentException("owner must be an instance of UserEntity");
    }

    /**
     * Retrieve this {@link Priceable}'s price.
     *
     * @return The price.
     */
    @Override
    public double getPrice() {

        return this.price;
    }

    /**
     * Define this {@link Priceable}'s price.
     *
     * @param price
     *         The price.
     */
    @Override
    public void setPrice(double price) {

        this.price = price;
    }

    /**
     * Retrieve this {@link Repeatable}'s repetition interval.
     *
     * @return The time.
     */
    @Override
    public Long getRepeatEvery() {

        return this.repeatCron;
    }

    /**
     * Define this {@link Repeatable}'s repetition interval.
     *
     * @param aLong
     *         The time.
     */
    @Override
    public void setRepeatEvery(Long aLong) {

        this.repeatCron = aLong;
    }
}
