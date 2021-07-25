package fr.alexpado.bots.cmb.modules.crossout.models.game;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.api.RarityEndpoint;
import fr.alexpado.bots.cmb.bot.DiscordBot;
import fr.alexpado.bots.cmb.enums.WatcherType;
import fr.alexpado.bots.cmb.interfaces.translatable.TranslatableJSONModel;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.modules.crossout.models.Watcher;
import fr.alexpado.bots.cmb.tools.TranslatableWatcher;
import fr.alexpado.bots.cmb.tools.Utilities;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.*;

@Getter
public class Item extends TranslatableJSONModel {

    private int id;

    /**
     * @deprecated The field `name` is no longer updated. Use `availableName` instead.
     */
    @Deprecated
    private String   name;
    private String   availableName;
    private String   description;
    private boolean  removed;
    private boolean  craftable;
    private int      sellPrice;
    private int      buyPrice;
    private int      craftingSellSum;
    private int      craftingBuySum;
    private long     lastUpdate;
    private Category category;
    private Faction  faction;
    private Rarity   rarity;
    private Type     type;

    public Item(CrossoutConfiguration config, JSONObject dataSource) throws Exception {

        super(config, dataSource);
        RarityEndpoint   endpoint       = new RarityEndpoint(config.getApiHost());
        Optional<Rarity> optionalRarity = endpoint.getOne(this.rarity.getId());
        optionalRarity.ifPresent(value -> this.rarity = value);
    }

    public Item(CrossoutConfiguration config, JSONObject dataSource, HashMap<Integer, Rarity> rarities) throws Exception {

        super(config, dataSource);
        if (this.rarity.getId() != 0) {
            this.rarity = rarities.get(this.rarity.getId());
        }
    }

    public static Optional<Item> from(CrossoutConfiguration config, JSONObject dataSource, HashMap<Integer, Rarity> rarities) {

        try {
            if (rarities != null) {
                return Optional.of(new Item(config, dataSource, rarities));
            }
            return Optional.of(new Item(config, dataSource));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Optional<Item> from(CrossoutConfiguration config, JSONObject dataSource) {

        return Item.from(config, dataSource, null);
    }

    @Override
    public List<String> getRequiredTranslation() {

        return Arrays.asList(Translation.GENERAL_INVITE, Translation.GENERAL_CURRENCY, Translation.MARKET_BUY, Translation.MARKET_CRAFTS_BUY, Translation.MARKET_SELL, Translation.MARKET_CRAFTS_SELL, WatcherType.NORMAL
                .getTranslation(), WatcherType.BUY_OVER.getTranslation(), WatcherType.BUY_UNDER.getTranslation(), WatcherType.SELL_OVER
                .getTranslation(), WatcherType.SELL_UNDER.getTranslation(), Translation.WATCHERS_OTHER, Translation.ITEMS_REMOVED, Translation.ITEMS_UNAVAILABLE);
    }

    @Override
    public boolean reload(JSONObject dataSource) {

        try {
            this.id   = dataSource.getInt("id");
            this.name = dataSource.getString("name");

            this.availableName = dataSource.getString("availableName");

            if (dataSource.get("description") == JSONObject.NULL) {
                this.description = "";
            } else {
                this.description = Utilities.removeHTML(dataSource.getString("description"));
            }

            if (dataSource.getInt("rarityId") == 0) {
                this.rarity = RarityEndpoint.getDefaultRarity();
            } else {
                this.rarity = new Rarity(dataSource.getInt("rarityId"), dataSource.getString("rarityName"));
            }

            this.sellPrice       = dataSource.getInt("sellPrice");
            this.buyPrice        = dataSource.getInt("buyPrice");
            this.craftingSellSum = dataSource.getInt("craftingSellSum");
            this.craftingBuySum  = dataSource.getInt("craftingBuySum");
            this.removed         = dataSource.getInt("removed") == 1;
            this.craftable       = !(this.craftingSellSum == 0 || this.craftingBuySum == 0);

            this.lastUpdate = Instant.parse(dataSource.getString("timestamp") + ".000Z").toEpochMilli() / 1000;
            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    private String getThumbnailUrl() {

        Date   today        = Calendar.getInstance().getTime();
        String thumbnailUrl = "https://crossoutdb.com/img/items/%s.png?d=%tY%tm%td";
        return String.format(thumbnailUrl, this.getId(), today, today, today);
    }

    private String getWebUrl() {

        String webUrl = "https://crossoutdb.com/item/%s?ref=crossoutmarketbot";
        return String.format(webUrl, this.getId());
    }

    public EmbedBuilder getDiffEmbed(JDA jda, Watcher watcher, TranslatableWatcher translatableWatcher) {

        int sellPrice = watcher.getSellPrice();
        int buyPrice  = watcher.getBuyPrice();

        EmbedBuilder builder      = this.getRawEmbed(jda);
        String       watcherTitle = translatableWatcher.toString();

        int idCutSize = String.valueOf(this.getId()).length() + 2;
        builder.setAuthor(watcherTitle.substring(idCutSize), null, jda.getSelfUser().getAvatarUrl());

        if (!this.removed) {
            String currentSellPrice = Utilities.money(this.sellPrice, this.getTranslation(Translation.GENERAL_CURRENCY));
            String currentBuyPrice  = Utilities.money(this.buyPrice, this.getTranslation(Translation.GENERAL_CURRENCY));

            String diffSellPrice = Utilities.money(this.sellPrice - sellPrice, "");
            String diffBuyPrice  = Utilities.money(this.buyPrice - buyPrice, "");

            builder.addField(this.getTranslation(Translation.MARKET_BUY), String.format("%s ( %s )", currentSellPrice, diffSellPrice), true);
            builder.addField(this.getTranslation(Translation.MARKET_SELL), String.format("%s ( %s )", currentBuyPrice, diffBuyPrice), true);
        }

        return builder;
    }

    public EmbedBuilder getAsEmbed(JDA jda) {

        EmbedBuilder builder = this.getRawEmbed(jda);
        builder.setTitle(this.availableName, String.format("https://crossoutdb.com/item/%s?ref=crossoutmarketbot", this.id));

        if (!this.removed) {
            builder.addField(this.getTranslation(Translation.MARKET_BUY), Utilities.money(this.sellPrice, this.getTranslation(Translation.GENERAL_CURRENCY)), true);
            builder.addField(this.getTranslation(Translation.MARKET_SELL), Utilities.money(this.buyPrice, this.getTranslation(Translation.GENERAL_CURRENCY)), true);
            if (this.craftable) {
                builder.addField("", "", true);
                builder.addField(this.getTranslation(Translation.MARKET_CRAFTS_BUY), Utilities.money(this.craftingSellSum, this
                        .getTranslation(Translation.GENERAL_CURRENCY)), true);
                builder.addField(this.getTranslation(Translation.MARKET_CRAFTS_SELL), Utilities.money(this.craftingBuySum, this
                        .getTranslation(Translation.GENERAL_CURRENCY)), true);
                builder.addField("", "", true);
            }
        }

        return builder;
    }

    private EmbedBuilder getRawEmbed(JDA jda) {

        EmbedBuilder builder  = new EmbedBuilder();
        String       chartUrl = this.getConfig().getChartUrl();

        builder.setAuthor(this.getTranslation(Translation.GENERAL_INVITE), DiscordBot.INVITE, jda.getSelfUser()
                                                                                                 .getAvatarUrl());
        builder.setTitle(this.name, String.format("https://crossoutdb.com/item/%s?ref=crossoutmarketbot", this.id));
        builder.setDescription(this.description);

        builder.setThumbnail(String.format("https://crossoutdb.com/img/items/%s.png?ref=crossoutmarketbot&v=%s", this.id, LocalDateTime
                .now()
                .getDayOfYear()));
        builder.setImage(String.format(chartUrl, this.lastUpdate, this.id));

        if (this.removed) {
            builder.addField(this.getTranslation(Translation.ITEMS_REMOVED), this.getTranslation(Translation.ITEMS_UNAVAILABLE), true);
        }

        if (this.rarity == null) {
            builder.setColor(Color.BLACK);
        } else {
            builder.setColor(this.rarity.getColor());
        }

        return builder;
    }

    @Override
    public String toString() {

        return this.availableName;
    }

}
