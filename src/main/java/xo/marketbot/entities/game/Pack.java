package xo.marketbot.entities.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.json.JSONArray;
import org.json.JSONObject;
import xo.marketbot.XoMarketApplication;
import xo.marketbot.entities.interfaces.common.Identifiable;
import xo.marketbot.entities.interfaces.common.Marchantable;
import xo.marketbot.entities.interfaces.common.Nameable;
import xo.marketbot.entities.interfaces.game.IPack;
import xo.marketbot.i18n.TranslationProvider;
import xo.marketbot.library.services.translations.annotations.I18N;
import xo.marketbot.tools.Utilities;

import javax.persistence.Embeddable;
import java.awt.*;

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

    @I18N(TranslationProvider.MARKET_BUY)
    private String marketBuy;
    @I18N(TranslationProvider.MARKET_SELL)
    private String marketSell;
    @I18N(TranslationProvider.MARKET_CRAFTS_BUY)
    private String marketCraftBuy;
    @I18N(TranslationProvider.MARKET_CRAFTS_SELL)
    private String marketCraftSell;
    @I18N(TranslationProvider.GENERAL_CURRENCY)
    private String generalCurreny;
    @I18N(TranslationProvider.GENERAL_INVITE)
    private String generalInvite;
    @I18N(TranslationProvider.PACKS_PRICE)
    private String packPrice;

    public Pack(JSONObject source) {

        this.id        = source.getInt("id");
        this.key       = source.getString("key");
        this.name      = source.getString("name");
        this.rawCoins  = source.getDouble("rawcoins");
        this.sellPrice = source.getDouble("sellsum") / 100;
        this.buyPrice  = source.getDouble("buysum") / 100;

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

    /**
     * Retrieve an {@link EmbedBuilder} representation of the current {@link Embeddable} instance.
     *
     * @param jda
     *         {@link JDA} instance to use to access the current bot state.
     *
     * @return An {@link EmbedBuilder}.
     */
    @Override
    public EmbedBuilder toEmbed(JDA jda) {

        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(this.generalInvite, XoMarketApplication.INVITE, jda.getSelfUser().getAvatarUrl());
        builder.setTitle(this.name, String.format("https://crossoutdb.com/packs?ref=crossoutmarketbot#pack=%s", this.getKey()));

        builder.addField(this.marketBuy, Utilities.money(this.sellPrice, this.generalCurreny), true);
        builder.addField(this.marketSell, Utilities.money(this.buyPrice, this.generalCurreny), true);

        builder.addBlankField(true);

        if (this.priceUsd != 0) {

            builder.addField(this.packPrice, String.format("%s\n%s\n%s\n%s", this.getPriceLine(this.priceUsd, "USD"), this.getPriceLine(this.priceEur, "EUR"), this
                    .getPriceLine(this.priceGbp, "GBP"), this.getPriceLine(this.priceRub, "RUB")), true);

            double value = this.buyPrice + this.rawCoins;

            builder.addField("", String.format("%s\n%s\n%s\n%s", Utilities.money(value / (this.priceUsd / 100f), "Coins/USD"), Utilities.money(value / (this.priceEur / 100f), "Coins/EUR"), Utilities
                    .money(value / (this.priceGbp / 100f), "Coins/GBP"), Utilities.money(value / (this.priceRub / 100f), "Coins/RUB")), true);
            builder.addField("", "", true);
        }

        builder.setImage(this.getPackPicture());
        builder.setColor(Color.WHITE);

        return builder;
    }

    private String getPriceLine(double price, String currency) {

        return Utilities.money((price / 100), " " + currency);
    }

    private String getPackPicture() {

        return String.format("https://crossoutdb.com/img/premiumpackages/%s.jpg", this.getKey());
    }
}
