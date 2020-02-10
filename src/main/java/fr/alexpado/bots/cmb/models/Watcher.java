package fr.alexpado.bots.cmb.models;

import fr.alexpado.bots.cmb.AppConfig;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.models.game.Item;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import fr.alexpado.bots.cmb.tools.TranslatableWatcher;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@Getter
public class Watcher {

    public Watcher() {
        super();
    }

    public static Watcher createFor(DiscordUser user, Item item) {
        Watcher watcher = new Watcher();
        watcher.setUser(user);
        watcher.loadItem(item);
        return watcher;
    }

    public TranslatableWatcher getTranslatableWatcher(AppConfig config, String language) throws MissingTranslationException {
        return new TranslatableWatcher(config, this, language);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private DiscordUser user;

    private int itemId;

    private String itemName;

    private float sellPrice;

    private float buyPrice;

    private int watcherType;

    private float price;

    private long repeatEvery = 300000;

    private long lastExecution = System.currentTimeMillis();

    @Override
    public String toString() {
        return String.format("[%s] %s", this.id, this.itemName);
    }

    public void loadItem(Item item) {
        this.setItemId(item.getId());
        this.setItemName(item.getName());
        this.setSellPrice(item.getSellPrice() / 100.0f);
        this.setBuyPrice(item.getBuyPrice() / 100.0f);
    }
}
