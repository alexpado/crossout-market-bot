package xo.marketbot.entities.discord;

import fr.alexpado.xodb4j.interfaces.IItem;
import fr.alexpado.xodb4j.interfaces.common.Identifiable;
import fr.alexpado.xodb4j.interfaces.common.Merchantable;
import fr.alexpado.xodb4j.interfaces.common.Nameable;
import jakarta.persistence.*;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.NotNull;
import xo.marketbot.entities.interfaces.crossout.IWatcher;
import xo.marketbot.enums.WatcherTrigger;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.tools.TimeConverter;
import xo.marketbot.tools.Utilities;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Watcher implements IWatcher, Comparable<Watcher> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer        id;
    private String         name;
    @Enumerated(EnumType.STRING)
    private WatcherTrigger trigger;
    private Integer        itemId;
    private Double         priceReference;
    private Double         marketSell;
    private Double         marketBuy;
    private Integer        buyOrders;
    private Integer        sellOffers;
    @ManyToOne
    private UserEntity     owner;
    private boolean        regular;
    private long           timing;
    private LocalDateTime  lastExecution;
    private int            failureCount;

    public Watcher() {
        // Default constructor
    }

    public Watcher(TranslationContext context, UserEntity user, IItem item, WatcherTrigger trigger, Double priceReference, long timing, boolean regular) {

        this.trigger        = trigger;
        this.itemId         = item.getId();
        this.priceReference = priceReference;
        this.marketSell     = item.getMarketSell();
        this.marketBuy      = item.getMarketBuy();
        this.sellOffers     = item.getSellOffers();
        this.buyOrders      = item.getBuyOrders();
        this.owner          = user;
        this.regular        = regular;
        this.timing         = timing;

        if (this.getTrigger() != WatcherTrigger.EVERYTIME && this.getPriceReference() == null) {
            throw new IllegalStateException("The trigger type requires a price reference.");
        }

        if (this.regular) {
            this.lastExecution = Utilities.toNormalizedDateTime(LocalDateTime.now(), this.getTiming());
        } else {
            this.lastExecution = LocalDateTime.now().withSecond(0).withNano(0);
        }

        this.name = switch (this.getTrigger()) {
            case SELL_UNDER, SELL_OVER, BUY_OVER, BUY_UNDER -> String.format(context.getTranslation(this.getTrigger()
                                                                                                        .getTranslationKey()), item.getName(), this.priceReference, new TimeConverter(this.timing));
            case EVERYTIME -> String.format("%s every %s", item.getName(), new TimeConverter(this.timing));
        };
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
     * Retrieve the amount of money needed to buy this {@link Merchantable}.
     *
     * @return The buy price
     */
    @Override
    public double getMarketSell() {

        return this.marketSell;
    }

    /**
     * Define the amount of money needed to buy this {@link Merchantable}.
     *
     * @param price
     *         The buy price.
     */
    @Override
    public void setMarketSell(double price) {

        this.marketSell = price;
    }

    /**
     * Retrieve the amount of money obtainable by selling this {@link Merchantable}.
     *
     * @return The sell price
     */
    @Override
    public double getMarketBuy() {

        return this.marketBuy;
    }

    /**
     * Define the amount of money obtainable by selling this {@link Merchantable}
     *
     * @param price
     *         The sell price
     */
    @Override
    public void setMarketBuy(double price) {

        this.marketBuy = price;
    }

    /**
     * Retrieve the amount of sell offers available for this {@link Merchantable}.
     *
     * @return The sell offers amount.
     */
    @Override
    public int getSellOffers() {

        return this.sellOffers;
    }

    /**
     * Define the amount of sell offers available for this {@link Merchantable}.
     *
     * @param sellOffers
     *         The sell offers amount.
     */
    @Override
    public void setSellOffers(int sellOffers) {

        this.sellOffers = sellOffers;
    }

    /**
     * Retrieve the amount of buy orders available for this {@link Merchantable}.
     *
     * @return The buy orders amount.
     */
    @Override
    public int getBuyOrders() {

        return this.buyOrders;
    }

    /**
     * Define the amount of buy orders available for this {@link Merchantable}.
     *
     * @param buyOrders
     *         The buy orders amount.
     */
    @Override
    public void setBuyOrders(int buyOrders) {

        this.buyOrders = buyOrders;
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
     * Retrieve this {@link IWatcher} trigger.
     *
     * @return A {@link WatcherTrigger}.
     */
    @Override
    public WatcherTrigger getTrigger() {

        return this.trigger;
    }

    /**
     * Define this {@link IWatcher} trigger.
     *
     * @param trigger
     *         A {@link WatcherTrigger}.
     */
    @Override
    public void setTrigger(WatcherTrigger trigger) {

        this.trigger = trigger;

        if (this.trigger == WatcherTrigger.EVERYTIME) {
            this.setPriceReference(null);
        }
    }

    /**
     * Retrieve the {@link IItem}'s ID that this {@link IWatcher} is set to.
     *
     * @return An {@link IItem}'s ID.
     */
    @Override
    public int getItemId() {

        return this.itemId;
    }

    /**
     * Define the {@link IItem}'s ID that this {@link IWatcher} is set to.
     *
     * @param id
     *         An {@link IItem}'s ID.
     */
    @Override
    public void setItemId(int id) {

        this.itemId = id;
    }

    /**
     * Retrieve the price limit which will trigger this {@link IWatcher} depending on its {@link #getTrigger()}. This
     * will most likely return null when {@link #getTrigger()} is set to {@link WatcherTrigger#EVERYTIME}.
     *
     * @return The price reference.
     */
    @Override
    public Double getPriceReference() {

        return this.priceReference;
    }

    /**
     * Define the price limit which will trigger this {@link IWatcher}. If {@link #getTrigger()} is
     * {@link WatcherTrigger#EVERYTIME}, this will most likely have any effect.
     *
     * @param price
     *         The price reference.
     */
    @Override
    public void setPriceReference(Double price) {

        this.priceReference = price;
    }

    /**
     * Retrieve the {@link UserEntity} owner of this {@link IWatcher}.
     *
     * @return The {@link UserEntity}.
     */
    @Override
    public UserEntity getOwner() {

        return this.owner;
    }

    /**
     * Define the {@link UserEntity} owner of this {@link IWatcher}.
     *
     * @param owner
     *         The {@link UserEntity}.
     */
    @Override
    public void setOwner(UserEntity owner) {

        this.owner = owner;
    }

    /**
     * Check if this {@link IWatcher} will be run on a regular basis.
     *
     * @return True if regular, false otherwise.
     */
    @Override
    public boolean isRegular() {

        return this.regular;
    }

    /**
     * Define if this {@link IWatcher} should be run on a regular basis.
     *
     * @param regular
     *         True if regular, false otherwise.
     */
    @Override
    public void setRegular(boolean regular) {

        this.regular = true;
    }

    /**
     * Retrieve the timing (interval) of this {@link IWatcher}.
     *
     * @return The timing (interval) in seconds.
     */
    @Override
    public long getTiming() {

        return this.timing;
    }

    /**
     * Define the timing (interval) of this {@link IWatcher}.
     *
     * @param timing
     *         The timing (interval) in seconds.
     */
    @Override
    public void setTiming(long timing) {

        this.timing = timing;
    }

    /**
     * Retrieve the last execution date of this {@link IWatcher}.
     *
     * @return The last execution date.
     */
    @Override
    public LocalDateTime getLastExecution() {

        return this.lastExecution;
    }

    /**
     * Define the last execution date of this {@link IWatcher}.
     *
     * @param lastExecution
     *         The last execution date.
     */
    @Override
    public void setLastExecution(LocalDateTime lastExecution) {

        if (this.isRegular()) {
            this.lastExecution = Utilities.toNormalizedDateTime(lastExecution, this.timing);
        } else {
            this.lastExecution = lastExecution;
        }
    }

    /**
     * Retrieve the failure count encountered during this watcher execution.
     *
     * @return The failure count.
     */
    @Override
    public int getFailureCount() {

        return this.failureCount;
    }

    /**
     * Define this failure count encountered during this watcher execution.
     *
     * @param failureCount
     *         The failure count.
     */
    @Override
    public void setFailureCount(int failureCount) {

        this.failureCount = failureCount;
    }

    /**
     * Merge data from the provided {@link Merchantable} into this {@link Watcher}.
     *
     * @param marchantable
     *         The {@link Merchantable}.
     */
    public void refresh(Merchantable marchantable) {

        this.setMarketSell(marchantable.getMarketSell());
        this.setMarketBuy(marchantable.getMarketBuy());
        this.setBuyOrders(marchantable.getBuyOrders());
        this.setSellOffers(marchantable.getSellOffers());
    }

    @Override
    public MessageEmbed.Field toField() {

        boolean isFailed = this.getFailureCount() >= 3;

        if (isFailed) {
            return new MessageEmbed.Field(this.getName(), "*[FAILURE]* ID " + this.getId(), false);
        }
        return new MessageEmbed.Field(this.getName(), "ID " + this.getId(), false);
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Watcher watcher) {
            return this.getId().equals(watcher.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.getId());
    }

    public LocalDateTime getNextExecution() {

        LocalDateTime next = this.getLastExecution().plusSeconds(this.getTiming());
        return isRegular() ? Utilities.toNormalizedDateTime(next, this.getTiming()) : next;
    }

    public boolean isLate(LocalDateTime now) {

        return now.isAfter(this.getNextExecution().plusSeconds(1));
    }

    @Override
    public int compareTo(@NotNull Watcher other) {

        return this.id.compareTo(other.getId());
    }

}
