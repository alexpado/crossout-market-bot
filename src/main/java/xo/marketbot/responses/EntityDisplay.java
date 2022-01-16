package xo.marketbot.responses;

import net.dv8tion.jda.api.JDA;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.entities.game.Item;
import xo.marketbot.entities.game.Pack;
import xo.marketbot.entities.interfaces.game.IItem;
import xo.marketbot.entities.interfaces.game.IPack;
import xo.marketbot.tools.Utilities;
import xo.marketbot.xodb.XoDB;

import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

public class EntityDisplay extends DisplayTemplate {

    private static final String XODB_ISSUE_CHANNEL         = "https://discord.com/channels/467736507336097814/467737953721122836";
    private static final String EMBED_DISPLAY_HEADER       = "**%s**\n[View on CrossoutDB](%s) • [Report an issue](%s)";
    private static final String EMBED_DISPLAY_HEADER_NO_XO = "**%s**\n[Report an issue](%s)";

    public EntityDisplay(JDA jda, XoDB xoDB, IItem item) {

        super(jda, String.format(EMBED_DISPLAY_HEADER, item.getName(), Item.toXoDBLink(item.getId()), XODB_ISSUE_CHANNEL));
        this.appendDescription(item.getDescription());
        this.setThumbnail(Item.toXoDBThumbnail(item.getId(), item.getLastUpdate()));

        if (item.isRemoved()) {
            this.appendDescription("\n\n");
            this.appendDescription("*This item has been removed.*");
        } else {

            this.addField("Market Sell:", Utilities.money(item.getMarketSell(), "Coins"), true);
            this.addField("Market Buy:", Utilities.money(item.getMarketBuy(), "Coins"), true);
            this.addBlankField(true);

            if (item.isCraftable()) {
                this.addField("Market Buy Craft Items:", Utilities.money(item.getBuyCraftPrice(), "Coins"), true);
                this.addField("Market Sell Craft Items:", Utilities.money(item.getSellCraftPrice(), "Coins"), true);
                this.addBlankField(true);
            }
        }

        this.setColor(item.getRarity().getColor());
        this.setImage(String.format(xoDB.getChartUrl(), item.getLastUpdate().toEpochSecond(ZoneOffset.UTC), item.getId()));
    }

    public EntityDisplay(JDA jda, IPack pack) {

        super(jda, String.format(EMBED_DISPLAY_HEADER, pack.getName(), Pack.toXoDBLink(pack.getKey()), XODB_ISSUE_CHANNEL));

        this.addField("Market Sell:", Utilities.money(pack.getMarketSell(), "Coins"), true);
        this.addField("Market Buy:", Utilities.money(pack.getMarketBuy(), "Coins"), true);
        this.addBlankField(true);

        if (pack.getPriceUSD() > 0) {
            double coinBase = pack.getMarketBuy() + pack.getRawCoins();

            List<String> priceMoney = new ArrayList<>();
            List<String> priceCoins = new ArrayList<>();

            priceMoney.add(Utilities.money(pack.getPriceUSD() / 100.0, "USD"));
            priceMoney.add(Utilities.money(pack.getPriceEUR() / 100.0, "EUR"));
            priceMoney.add(Utilities.money(pack.getPriceGBP() / 100.0, "GBP"));
            priceMoney.add(Utilities.money(pack.getPriceRUB() / 100.0, "RUB"));

            priceCoins.add(Utilities.money(coinBase / (pack.getPriceUSD() / 100.0), "Coins/USD"));
            priceCoins.add(Utilities.money(coinBase / (pack.getPriceEUR() / 100.0), "Coins/EUR"));
            priceCoins.add(Utilities.money(coinBase / (pack.getPriceGBP() / 100.0), "Coins/GBP"));
            priceCoins.add(Utilities.money(coinBase / (pack.getPriceRUB() / 100.0), "Coins/RUB"));

            this.addField("Pack Prices:", String.join("\n", priceMoney), true);
            this.addField("", String.join("\n", priceCoins), true);
            this.addBlankField(true);
        }

        this.setImage(Pack.toXoDBThumbnail(pack.getKey()));


    }

    public EntityDisplay(JDA jda, XoDB xoDB, Watcher watcher, IItem item) {

        super(jda, String.format(EMBED_DISPLAY_HEADER_NO_XO, watcher.getName(), XODB_ISSUE_CHANNEL));
        this.setThumbnail(Item.toXoDBThumbnail(item.getId(), item.getLastUpdate()));

        String newMarketSell  = Utilities.money(item.getMarketSell() / 100.0, "Coins");
        String newMarketBuy   = Utilities.money(item.getMarketBuy() / 100.0, "Coins");
        String marketSellDiff = Utilities.money((item.getMarketSell() - watcher.getMarketSell()) / 100.0, "Coins");
        String marketBuyDiff  = Utilities.money((item.getMarketBuy() - watcher.getMarketBuy()) / 100.0, "Coins");

        String priceFormat = "%s (%s)";

        this.addField("Market Sell:", String.format(priceFormat, newMarketSell, marketSellDiff), true);
        this.addField("Market Buy:", String.format(priceFormat, newMarketBuy, marketBuyDiff), true);

        this.setColor(item.getRarity().getColor());
        this.setImage(String.format(xoDB.getChartUrl(), item.getLastUpdate().toEpochSecond(ZoneOffset.UTC), item.getId()));
    }

}
