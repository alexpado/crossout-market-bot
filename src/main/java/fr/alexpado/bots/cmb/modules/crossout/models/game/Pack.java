package fr.alexpado.bots.cmb.modules.crossout.models.game;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.bot.DiscordBot;
import fr.alexpado.bots.cmb.interfaces.translatable.TranslatableJSONModel;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.tools.Utilities;
import lombok.Getter;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Getter
public class Pack extends TranslatableJSONModel {

    private int    id;
    private String key;
    private String name;
    private int    sellSum;
    private int    buySum;

    private int usdPrice = 0;
    private int eurPrice = 0;
    private int gbpPrice = 0;
    private int rubPrice = 0;

    private int rawCoins = 0;

    public Pack(CrossoutConfiguration config, JSONObject source) {

        super(config);
        this.reload(source);
    }

    public static Optional<Pack> from(CrossoutConfiguration config, JSONObject dataSource) {

        try {
            return Optional.of(new Pack(config, dataSource));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public boolean reload(JSONObject dataSource) {

        try {
            this.id       = dataSource.getInt("id");
            this.key      = dataSource.getString("key");
            this.name     = dataSource.getString("name");
            this.sellSum  = dataSource.getInt("sellsum");
            this.buySum   = dataSource.getInt("buysum");
            this.rawCoins = dataSource.getInt("rawcoins");

            JSONObject appPrices = dataSource.getJSONObject("appprices");

            if (appPrices.get("prices") != JSONObject.NULL) {
                JSONArray prices = dataSource.getJSONObject("appprices").getJSONArray("prices");

                for (int i = 0 ; i < prices.length() ; i++) {
                    JSONObject value = prices.getJSONObject(i);

                    switch (value.getString("currencyabbriviation")) {
                        case "usd":
                            this.usdPrice = value.getInt("final");
                            break;
                        case "eur":
                            this.eurPrice = value.getInt("final");
                            break;
                        case "gbp":
                            this.gbpPrice = value.getInt("final");
                            break;
                        case "rub":
                            this.rubPrice = value.getInt("final");
                            break;
                    }

                }
            }

            return true;
        } catch (JSONException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getPackPicture() {

        return String.format("https://crossoutdb.com/img/premiumpackages/%s.jpg", this.getKey());
    }

    @Override
    public List<String> getRequiredTranslation() {

        return Arrays.asList(Translation.GENERAL_INVITE, Translation.MARKET_BUY, Translation.MARKET_SELL, Translation.PACKS_PRICE);
    }

    public EmbedBuilder getAsEmbed(JDA jda) {

        EmbedBuilder builder = new EmbedBuilder();

        builder.setAuthor(this.getTranslation(Translation.GENERAL_INVITE), DiscordBot.INVITE, jda.getSelfUser()
                                                                                                 .getAvatarUrl());
        builder.setTitle(this.name, String.format("https://crossoutdb.com/packs?ref=crossoutmarketbot#pack=%s", this.getKey()));

        builder.addField(this.getTranslation(Translation.MARKET_SELL), Utilities.money(this.buySum, ""), true);
        builder.addField(this.getTranslation(Translation.MARKET_BUY), Utilities.money(this.sellSum, ""), true);

        builder.addField("", "", true);

        if (this.usdPrice != 0) {
            builder.addField(this.getTranslation(Translation.PACKS_PRICE), String.format("%s\n%s\n%s\n%s", this.getPriceLine(this.usdPrice, "USD"), this
                    .getPriceLine(this.eurPrice, "EUR"), this.getPriceLine(this.gbpPrice, "GBP"), this.getPriceLine(this.rubPrice, "RUB")), true);

            float value = this.buySum / 100f + this.rawCoins;
            System.out.println(value);

            builder.addField("", String.format("%s\n%s\n%s\n%s", Utilities.money(value / (this.usdPrice / 100f), "Coins/USD"), Utilities
                    .money(value / (this.eurPrice / 100f), "Coins/EUR"), Utilities.money(value / (this.gbpPrice / 100f), "Coins/GBP"), Utilities
                    .money(value / (this.rubPrice / 100f), "Coins/RUB")), true);
            builder.addField("", "", true);
        }

        builder.setImage(this.getPackPicture());
        builder.setColor(Color.WHITE);

        return builder;
    }

    private String getPriceLine(int price, String currency) {

        return Utilities.money(price, " " + currency);
    }

    @Override
    public String toString() {

        return this.name;
    }

}
