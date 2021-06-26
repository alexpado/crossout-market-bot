package xo.marketbot.entities.discord;

import xo.marketbot.entities.interfaces.game.IItem;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.library.services.translations.annotations.I18N;
import xo.marketbot.tools.TimeConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Watcher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String name;

    @Column(length = 20)
    private String  type;
    private Integer itemId;
    private String  itemName;
    private Double  price;
    private Double  sellPrice;
    private Double  buyPrice;

    @ManyToOne
    private UserEntity    owner;
    private boolean       regular;
    private long          timing;
    private LocalDateTime lastExecution;

    // <editor-fold desc="I18N">

    @Transient
    @I18N(TranslationProvider.WATCHERS_BUY_OVER)
    private String watcherTypeBuyOver;

    @Transient
    @I18N(TranslationProvider.WATCHERS_BUY_UNDER)
    private String watcherTypeBuyUnder;

    @Transient
    @I18N(TranslationProvider.WATCHERS_SELL_OVER)
    private String watcherTypeSellOver;

    @Transient
    @I18N(TranslationProvider.WATCHERS_SELL_UNDER)
    private String watcherTypeSellUnder;

    @Transient
    @I18N(TranslationProvider.WATCHERS_NORMAL)
    private String watcherTypeNormal;

    @Transient
    @I18N(TranslationProvider.WATCHERS_OTHER)
    private String watcherTypeOther;

    // </editor-fold>

    public static Watcher create(UserEntity user, IItem item) {

        Watcher watcher = new Watcher();

        watcher.setOwner(user);
        watcher.setItemId(item.getId());
        watcher.setItemName(item.getName());
        watcher.setSellPrice(item.getSellPrice());
        watcher.setBuyPrice(item.getBuyPrice());

        return watcher;
    }

    public Integer getId() {

        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getType() {

        return type;
    }

    public void setType(String type) {

        this.type = type;
    }

    public Integer getItemId() {

        return itemId;
    }

    public void setItemId(Integer itemId) {

        this.itemId = itemId;
    }

    public String getItemName() {

        return itemName;
    }

    public void setItemName(String itemName) {

        this.itemName = itemName;
    }

    public Double getSellPrice() {

        return sellPrice;
    }

    public void setSellPrice(Double sellPrice) {

        this.sellPrice = sellPrice;
    }

    public Double getBuyPrice() {

        return buyPrice;
    }

    public void setBuyPrice(Double buyPrice) {

        this.buyPrice = buyPrice;
    }

    public UserEntity getOwner() {

        return owner;
    }

    public void setOwner(UserEntity owner) {

        this.owner = owner;
    }

    public boolean isRegular() {

        return regular;
    }

    public void setRegular(boolean regular) {

        this.regular = regular;
    }

    public long getTiming() {

        return timing;
    }

    public void setTiming(long timing) {

        this.timing = timing;
    }

    public LocalDateTime getLastExecution() {

        return lastExecution;
    }

    public void setLastExecution(LocalDateTime lastExecution) {

        this.lastExecution = lastExecution;
    }

    public Double getPrice() {

        return price;
    }

    public void setPrice(Double price) {

        this.price = price;
    }

    public String getDescription() {

        switch (this.type) {
            case "SELL_UNDER":
                return String.format(this.watcherTypeOther, this.itemName, String.format(this.watcherTypeSellUnder, this.price), new TimeConverter(this.timing));
            case "SELL_OVER":
                return String.format(this.watcherTypeOther, this.itemName, String.format(this.watcherTypeSellOver, this.price), new TimeConverter(this.timing));
            case "BUY_UNDER":
                return String.format(this.watcherTypeOther, this.itemName, String.format(this.watcherTypeBuyUnder, this.price), new TimeConverter(this.timing));
            case "BUY_OVER":
                return String.format(this.watcherTypeOther, this.itemName, String.format(this.watcherTypeBuyOver, this.price), new TimeConverter(this.timing));
            case "NORMAL":
                return String.format(this.watcherTypeNormal, this.itemName, new TimeConverter(this.timing));
            default:
                return "??? :(";
        }

    }
}
