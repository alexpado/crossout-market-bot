package fr.alexpado.bots.cmb.models;

import fr.alexpado.bots.cmb.bot.DiscordBot;
import fr.alexpado.bots.cmb.enums.WatcherTypes;
import fr.alexpado.bots.cmb.interfaces.TranslatableObject;
import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import fr.alexpado.bots.cmb.models.game.Item;
import fr.alexpado.bots.cmb.repositories.TranslationRepository;
import fr.alexpado.bots.cmb.throwables.TranslationException;
import fr.alexpado.bots.cmb.tools.TimeConverter;
import fr.alexpado.bots.cmb.tools.Utilities;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Entity
@Setter
@Getter
public class Watcher implements TranslatableObject {

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

    @SneakyThrows
    public String toString(String lang) {
        if (lang == null) {
            return this.toString();
        }

        WatcherTypes type = WatcherTypes.getFromId(this.watcherType);
        TranslationRepository tr = DiscordBot.getInstance().getTranslationRepository();

        String time = new TimeConverter(this.repeatEvery / 1000).toString();

        List<String> neededTranslation = Arrays.asList(Translation.CURRENCY, Translation.WATCHER_TYPE_ADVANCED, type.getTranslation());
        List<Translation> translations = tr.getNeededFromLanguage(neededTranslation, lang);

        if (neededTranslation.size() != translations.size()) {
            throw new TranslationException(neededTranslation.size(), translations.size());
        }

        HashMap<String, String> translationMap = new HashMap<>();

        translations.forEach(translation -> translationMap.put(translation.getTranslationKey(), translation.getText()));

        switch (type) {
            case SELL_OVER:
            case SELL_UNDER:
            case BUY_OVER:
            case BUY_UNDER:
                String advancedContent = String.format(translationMap.get(type.getTranslation()), Utilities.money(this.price, ""));
                String watcherTextContent = String.format(translationMap.get(Translation.WATCHER_TYPE_ADVANCED), this.itemName, advancedContent, time);
                return String.format("[%s] %s", this.id, watcherTextContent);
            case NORMAL:
                return String.format("[%s] %s", this.id, String.format(translationMap.get(Translation.WATCHER_TYPE_NORMAL), this.itemName, time));
        }

        return String.format("[%s] %s", this.id, this.itemName);
    }

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
