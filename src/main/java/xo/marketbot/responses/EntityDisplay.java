package xo.marketbot.responses;

import fr.alexpado.xodb4j.XoDBUtils;
import fr.alexpado.xodb4j.interfaces.IItem;
import fr.alexpado.xodb4j.interfaces.IPack;
import fr.alexpado.xodb4j.interfaces.IRarity;
import net.dv8tion.jda.api.JDA;
import xo.marketbot.configurations.interfaces.IMarketConfiguration;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.enums.PriceEmote;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.tools.TimeConverter;
import xo.marketbot.tools.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static xo.marketbot.services.i18n.TranslationService.*;

public class EntityDisplay extends DisplayTemplate {

    private static final String XODB_ISSUE_CHANNEL         = "https://discord.com/channels/467736507336097814/467737953721122836";
    private static final String EMBED_DISPLAY_HEADER       = "**%s**\n[View on CrossoutDB](%s) • [Report an issue](%s)";
    private static final String EMBED_DISPLAY_HEADER_NO_XO = "**%s**\n[Report an issue](%s)";

    public EntityDisplay(TranslationContext context, IMarketConfiguration configuration, JDA jda, IItem item) {

        super(context, jda, String.format(context.getTranslation(TR_EMBED__HEADER_FULL), item.getName(), XoDBUtils.getWebLink(item), XODB_ISSUE_CHANNEL));
        this.appendDescription(item.getDescription());
        this.setThumbnail(XoDBUtils.getImage(item));

        if (item.isRemoved()) {
            this.appendDescription("\n\n");
            this.appendDescription(context.getTranslation(TR_ITEM__REMOVED));
        } else {

            String currency    = context.getTranslation(TR_MARKET__CURRENCY);
            String priceFormat = context.getTranslation(TR_MARKET__PRICE);
            this.addField(context.getTranslation(TR_MARKET__SELL), priceFormat.formatted(item.getMarketSell(), currency, item.getSellOffers()), true);
            this.addField(context.getTranslation(TR_MARKET__BUY), priceFormat.formatted(item.getMarketBuy(), currency, item.getBuyOrders()), true);
            this.addBlankField(true);

            if (item.isCraftable()) {
                this.addField(context.getTranslation(TR_MARKET__CRAFT_SELL), Utilities.money(item.getSellCraftPrice(), currency), true);
                this.addField(context.getTranslation(TR_MARKET__CRAFT_BUY), Utilities.money(item.getBuyCraftPrice(), currency), true);
                this.addBlankField(true);
            }

            this.setImage(Utilities.createChartUrl(configuration, item, 5));
        }

        IRarity rarity = Optional.ofNullable(item.getRarity()).orElse(IRarity.DEFAULT);
        this.setColor(rarity.getColor());
    }

    public EntityDisplay(TranslationContext context, JDA jda, IPack pack) {

        super(context, jda, String.format(context.getTranslation(TR_EMBED__HEADER_FULL), pack.getName(), XoDBUtils.getWebLink(pack), XODB_ISSUE_CHANNEL));

        String currency = context.getTranslation(TR_MARKET__CURRENCY);
        this.addField(context.getTranslation(TR_MARKET__SELL), Utilities.money(pack.getMarketSell() / 100.0, currency), true);
        this.addField(context.getTranslation(TR_MARKET__BUY), Utilities.money(pack.getMarketBuy() / 100.0, currency), true);
        this.addBlankField(true);

        if (pack.getPriceUSD() > 0) {
            double coinBase = pack.getMarketBuy() + pack.getRawCoins();

            List<String> priceMoney = new ArrayList<>();
            List<String> priceCoins = new ArrayList<>();

            priceMoney.add(Utilities.money(pack.getPriceUSD() / 100.0, "USD"));
            priceMoney.add(Utilities.money(pack.getPriceEUR() / 100.0, "EUR"));
            priceMoney.add(Utilities.money(pack.getPriceGBP() / 100.0, "GBP"));
            priceMoney.add(Utilities.money(pack.getPriceRUB() / 100.0, "RUB"));

            priceCoins.add(Utilities.money(coinBase / (pack.getPriceUSD() / 100.0), currency + "/USD"));
            priceCoins.add(Utilities.money(coinBase / (pack.getPriceEUR() / 100.0), currency + "/EUR"));
            priceCoins.add(Utilities.money(coinBase / (pack.getPriceGBP() / 100.0), currency + "/GBP"));
            priceCoins.add(Utilities.money(coinBase / (pack.getPriceRUB() / 100.0), currency + "/RUB"));

            this.addField(context.getTranslation(TR_PACK__PRICE), String.join("\n", priceMoney), true);
            this.addField("", String.join("\n", priceCoins), true);
            this.addBlankField(true);
        }

        this.setImage(XoDBUtils.getImage(pack));
    }

    public EntityDisplay(TranslationContext context, IMarketConfiguration configuration, JDA jda, Watcher watcher, IItem item) {

        super(context, jda, String.format(context.getTranslation(TR_EMBED__HEADER_SIMPLE), watcher.getName(), XODB_ISSUE_CHANNEL));
        this.setThumbnail(XoDBUtils.getImage(item));

        String currency        = context.getTranslation(TR_MARKET__CURRENCY);
        String newMarketSell   = Utilities.money(item.getMarketSell(), currency);
        String newMarketBuy    = Utilities.money(item.getMarketBuy(), currency);
        String newMarketOffers = String.valueOf(item.getSellOffers());
        String newMarketOrders = String.valueOf(item.getBuyOrders());
        String marketSellDiff  = Utilities.money((item.getMarketSell() - watcher.getMarketSell()), currency);
        String marketBuyDiff   = Utilities.money((item.getMarketBuy() - watcher.getMarketBuy()), currency);
        String marketOfferDiff = String.valueOf(item.getSellOffers() - watcher.getSellOffers());
        String marketOrderDiff = String.valueOf(item.getBuyOrders() - watcher.getBuyOrders());

        this.appendDescription(String.format(
                context.getTranslation(watcher.getTrigger().getTranslationKey()),
                item.getName(),
                watcher.getPriceReference(),
                new TimeConverter(watcher.getTiming())
        ));

        String priceFormat = "%s\n%s %s";

        String sellEmote  = PriceEmote.with(item.getMarketSell(), watcher.getMarketSell(), false).getEmote(jda);
        String buyEmote   = PriceEmote.with(item.getMarketBuy(), watcher.getMarketBuy(), true).getEmote(jda);
        String offerEmote = PriceEmote.with(item.getSellOffers(), watcher.getSellOffers(), false).getEmote(jda);
        String orderEmote = PriceEmote.with(item.getBuyOrders(), watcher.getBuyOrders(), true).getEmote(jda);

        this.addField(context.getTranslation(TR_MARKET__SELL), String.format(priceFormat, newMarketSell, sellEmote, marketSellDiff), true);
        this.addField(context.getTranslation(TR_MARKET__BUY), String.format(priceFormat, newMarketBuy, buyEmote, marketBuyDiff), true);

        this.addBlankField(true);

        this.addField(context.getTranslation(TR_MARKET__SELL_OFFERS), String.format(priceFormat, newMarketOffers, offerEmote, marketOfferDiff), true);
        this.addField(context.getTranslation(TR_MARKET__BUY_ORDERS), String.format(priceFormat, newMarketOrders, orderEmote, marketOrderDiff), true);

        this.setImage(Utilities.createChartUrl(configuration, item, 5));
    }

}
