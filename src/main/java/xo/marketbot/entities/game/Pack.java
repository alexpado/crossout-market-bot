package xo.marketbot.entities.game;

import net.dv8tion.jda.api.entities.MessageEmbed;
import org.json.JSONArray;
import org.json.JSONObject;
import xo.marketbot.entities.interfaces.common.Identifiable;
import xo.marketbot.entities.interfaces.common.Marchantable;
import xo.marketbot.entities.interfaces.common.Nameable;
import xo.marketbot.entities.interfaces.game.IPack;
import xo.marketbot.tools.Utilities;

public class Pack implements IPack {

    private final Integer id;
    private final String  key;
    private final String  name;
    private final Double  sellPrice;
    private final Double  buyPrice;
    private final Double  rawCoins;
    private       Double  priceUsd;
    private       Double  priceEur;
    private       Double  priceGbp;
    private       Double  priceRub;

    public Pack(JSONObject source) {

        this.id        = source.getInt("id");
        this.key       = source.getString("key");
        this.name      = source.getString("name");
        this.rawCoins  = source.getDouble("rawcoins");
        this.sellPrice = source.getDouble("sellsum") / 100;
        this.buyPrice  = source.getDouble("buysum") / 100;

        JSONObject appPrices = source.getJSONObject("appprices");
        JSONArray  array     = appPrices.getJSONArray("prices");

        for (int i = 0; i < array.length(); i++) {
            JSONObject priceSource = array.getJSONObject(i);
            double     value       = priceSource.getDouble("final");

            switch (priceSource.getString("currencyabbriviation").toLowerCase()) {
                case "usd" -> this.priceUsd = value;
                case "eur" -> this.priceEur = value;
                case "gbp" -> this.priceGbp = value;
                case "rub" -> this.priceRub = value;
            }
        }

    }

    public static String toXoDBLink(String key) {

        return String.format("https://crossoutdb.com/packs?ref=crossoutmarketbot#%s", key);
    }

    public static String toXoDBThumbnail(String key) {

        return String.format("https://crossoutdb.com/img/premiumpackages/%s.jpg", key);
    }

    /**
     * Retrieve this {@link IPack}'s key identifier.
     *
     * @return A string
     */
    @Override
    public String getKey() {

        return this.key;
    }

    /**
     * Retrieve this {@link IPack}'s price in USD.
     *
     * @return The price
     */
    @Override
    public Double getPriceUSD() {

        return this.priceUsd;
    }

    /**
     * Retrieve this {@link IPack}'s price in EUR.
     *
     * @return The price
     */
    @Override
    public Double getPriceEUR() {

        return this.priceEur;
    }

    /**
     * Retrieve this {@link IPack}'s price in GBP.
     *
     * @return The price
     */
    @Override
    public Double getPriceGBP() {

        return this.priceGbp;
    }

    /**
     * Retrieve this {@link IPack}'s price in RUB.
     *
     * @return The price
     */
    @Override
    public Double getPriceRUB() {

        return this.priceRub;
    }

    /**
     * Retrieve this {@link IPack}'s value in coins.
     *
     * @return The value
     */
    @Override
    public Double getRawCoins() {

        return this.rawCoins;
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

        return this.sellPrice;
    }

    /**
     * Retrieve the amount of money obtainable by selling this {@link Marchantable}.
     *
     * @return The sell price
     */
    @Override
    public double getMarketBuy() {

        return this.buyPrice;
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

    private String getPriceLine(double price, String currency) {

        return Utilities.money((price / 100), " " + currency);
    }

    @Override
    public MessageEmbed.Field toField() {

        return new MessageEmbed.Field(this.getName(), String.format("%s • %s • %s • %s", this.getPriceLine(this.priceUsd, "USD"), this.getPriceLine(this.priceEur, "EUR"), this.getPriceLine(this.priceGbp, "GBP"), this.getPriceLine(this.priceRub, "RUB")), true);
    }

}
