package fr.alexpado.bots.cmb.cleaning.entities.game;

import fr.alexpado.bots.cmb.cleaning.interfaces.common.Identifiable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Marchantable;
import fr.alexpado.bots.cmb.cleaning.interfaces.common.Nameable;
import fr.alexpado.bots.cmb.cleaning.interfaces.game.IPack;
import org.json.JSONArray;
import org.json.JSONObject;

public class Pack implements IPack {

    private final Integer id;
    private final String  key;
    private final String  name;
    private final Double  sellPrice;
    private final Double  buyPrice;
    private       Double  priceUsd;
    private       Double  priceEur;
    private       Double  priceGbp;
    private       Double  priceRub;
    private       Double  rawCoins;

    public Pack(JSONObject source) {

        this.id        = source.getInt("id");
        this.key       = source.getString("key");
        this.name      = source.getString("name");
        this.rawCoins  = source.getDouble("rawcoins");
        this.sellPrice = source.getDouble("sellsum");
        this.buyPrice  = source.getDouble("buysum");

        JSONObject appPrices = source.getJSONObject("appprices");
        JSONArray  array     = appPrices.getJSONArray("prices");

        for (int i = 0 ; i < array.length() ; i++) {
            JSONObject priceSource = array.getJSONObject(i);
            double     value       = priceSource.getDouble("final");

            switch (priceSource.getString("currencyabbriviation")) {
                case "usd":
                    this.priceUsd = value;
                    break;
                case "eur":
                    this.priceEur = value;
                    break;
                case "gbp":
                    this.priceGbp = value;
                    break;
                case "rub":
                    this.priceRub = value;
            }
        }

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
    public double getBuyPrice() {

        return this.sellPrice;
    }

    /**
     * Retrieve the amount of money obtainable by selling this {@link Marchantable}.
     *
     * @return The sell price
     */
    @Override
    public double getSellPrice() {

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
}
