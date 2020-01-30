package fr.alexpado.bots.cmb.crossout.models;

import fr.alexpado.bots.cmb.crossout.models.discord.DiscordUser;

import javax.persistence.*;

@Entity
public class Watcher {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @OneToOne
    private DiscordUser user;

    private int itemId;
    private String itemName;
    private int sellPrice;
    private int buyPrice;
    private int watcherType;
    private int price;
    private long repeatEvery = 300000;
    private long lastExecution = System.currentTimeMillis();

    public String toString() {
        return String.format("[%s] %s", this.id, this.itemName);
    }

}
