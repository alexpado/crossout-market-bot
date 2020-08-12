package fr.alexpado.bots.cmb.modules.crossout.models;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.modules.crossout.models.game.Item;
import fr.alexpado.bots.cmb.modules.discord.models.DiscordUser;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;
import fr.alexpado.bots.cmb.tools.TranslatableWatcher;
import lombok.Getter;
import lombok.Setter;

import javax.annotation.Nullable;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
@Setter
@Getter
public class Watcher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne
    @NotNull
    private DiscordUser user;

    @NotNull
    private int itemId;

    private String itemName;

    @Nullable
    private String name;

    private int sellPrice;

    private int buyPrice;

    private int watcherType;

    private float price;

    private long repeatEvery = 300000;

    private long lastExecution = System.currentTimeMillis();

    public Watcher() {

        super();
    }

    public static Watcher createFor(DiscordUser user, Item item) {

        Watcher watcher = new Watcher();
        watcher.setUser(user);
        watcher.loadItem(item);
        return watcher;
    }

    public TranslatableWatcher getTranslatableWatcher(CrossoutConfiguration config, String language) throws MissingTranslationException {

        return new TranslatableWatcher(config, this, language);
    }

    @Override
    public String toString() {

        return String.format("[%s] %s", this.id, this.itemName);
    }

    public void loadItem(Item item) {

        this.setItemId(item.getId());
        this.setItemName(item.getName());
        this.setSellPrice(item.getSellPrice());
        this.setBuyPrice(item.getBuyPrice());
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) { return true; }
        if (!(o instanceof Watcher)) { return false; }
        Watcher watcher = (Watcher) o;
        return this.id.equals(watcher.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.id);
    }

}
