package xo.marketbot.responses;

import fr.alexpado.xodb4j.XoDBUtils;
import fr.alexpado.xodb4j.interfaces.IItem;
import fr.alexpado.xodb4j.interfaces.IPack;
import fr.alexpado.xodb4j.interfaces.IRarity;
import net.dv8tion.jda.api.JDA;
import xo.marketbot.configurations.interfaces.IEmojiConfiguration;
import xo.marketbot.configurations.interfaces.IMarketConfiguration;
import xo.marketbot.entities.discord.Watcher;
import xo.marketbot.services.i18n.TranslationContext;
import xo.marketbot.tools.TimeConverter;
import xo.marketbot.tools.Utilities;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static xo.marketbot.services.i18n.TranslationService.*;

public class EntityDisplay extends DisplayTemplate {

    public EntityDisplay(TranslationContext context, IMarketConfiguration configuration, JDA jda, IItem item) {

        super(context, jda, String.format(context.getTranslation(TR_EMBED__HEADER_FULL), item.getName(), XoDBUtils.getWebLink(item), configuration.getSupportServer()));
        this.appendDescription(item.getDescription());
        this.setThumbnail(XoDBUtils.getImage(item));

        if (item.isRemoved()) {
            this.appendDescription("\n\n");
            this.appendDescription(context.getTranslation(TR_ITEM__REMOVED));
        } else {

            String currency          = context.getTranslation(TR_MARKET__CURRENCY);
            String priceFormat       = context.getTranslation(TR_MARKET__PRICE);
            String simplePriceFormat = context.getTranslation(TR_MARKET__SIMPLE_PRICE);

            this.addField(context.getTranslation(TR_MARKET__SELL), priceFormat.formatted(item.getMarketSell(), currency, item.getSellOffers()), true);
            this.addField(context.getTranslation(TR_MARKET__BUY), priceFormat.formatted(item.getMarketBuy(), currency, item.getBuyOrders()), true);
            this.addBlankField(true);

            if (item.isCraftable()) {
                this.addField(context.getTranslation(TR_MARKET__CRAFT_SELL), simplePriceFormat.formatted(item.getSellCraftPrice(), currency), true);
                this.addField(context.getTranslation(TR_MARKET__CRAFT_BUY), simplePriceFormat.formatted(item.getBuyCraftPrice(), currency), true);
                this.addBlankField(true);
            }

            this.setImage(Utilities.createChartUrl(configuration, item, 5));
        }

        IRarity rarity = Optional.ofNullable(item.getRarity()).orElse(IRarity.DEFAULT);
        this.setColor(rarity.getColor());
    }

    public EntityDisplay(TranslationContext context, IMarketConfiguration configuration, JDA jda, IPack pack) {

        super(context, jda, String.format(context.getTranslation(TR_EMBED__HEADER_FULL), pack.getName(), XoDBUtils.getWebLink(pack), configuration.getSupportServer()));

        String simplePriceFormat = context.getTranslation(TR_MARKET__SIMPLE_PRICE);
        String currency          = context.getTranslation(TR_MARKET__CURRENCY);

        this.addField(context.getTranslation(TR_MARKET__SELL), simplePriceFormat.formatted(pack.getMarketSell() / 100.0, currency), true);
        this.addField(context.getTranslation(TR_MARKET__BUY), simplePriceFormat.formatted(pack.getMarketBuy() / 100.0, currency), true);
        this.addBlankField(true);

        if (pack.getPriceUSD() > 0) {
            double coinBase = pack.getMarketBuy() + pack.getRawCoins();

            List<String> priceMoney = new ArrayList<>();
            List<String> priceCoins = new ArrayList<>();

            priceMoney.add(simplePriceFormat.formatted(pack.getPriceUSD() / 100.0, "USD"));
            priceMoney.add(simplePriceFormat.formatted(pack.getPriceEUR() / 100.0, "EUR"));
            priceMoney.add(simplePriceFormat.formatted(pack.getPriceGBP() / 100.0, "GBP"));
            priceMoney.add(simplePriceFormat.formatted(pack.getPriceRUB() / 100.0, "RUB"));

            priceCoins.add(simplePriceFormat.formatted(coinBase / (pack.getPriceUSD() / 100.0), currency + "/USD"));
            priceCoins.add(simplePriceFormat.formatted(coinBase / (pack.getPriceEUR() / 100.0), currency + "/EUR"));
            priceCoins.add(simplePriceFormat.formatted(coinBase / (pack.getPriceGBP() / 100.0), currency + "/GBP"));
            priceCoins.add(simplePriceFormat.formatted(coinBase / (pack.getPriceRUB() / 100.0), currency + "/RUB"));

            this.addField(context.getTranslation(TR_PACK__PRICE), String.join("\n", priceMoney), true);
            this.addField("", String.join("\n", priceCoins), true);
            this.addBlankField(true);
        }

        this.setImage(XoDBUtils.getImage(pack));
    }

    public EntityDisplay(TranslationContext context, IMarketConfiguration configuration, IEmojiConfiguration emoji, JDA jda, Watcher watcher, IItem item) {

        super(context, jda, String.format(context.getTranslation(TR_EMBED__HEADER_SIMPLE), watcher.getName(), configuration.getSupportServer()));
        this.setThumbnail(XoDBUtils.getImage(item));

        String simplePriceFormat = context.getTranslation(TR_MARKET__SIMPLE_PRICE);

        String currency        = context.getTranslation(TR_MARKET__CURRENCY);
        String newMarketSell   = simplePriceFormat.formatted(item.getMarketSell(), currency);
        String newMarketBuy    = simplePriceFormat.formatted(item.getMarketBuy(), currency);
        String newMarketOffers = String.valueOf(item.getSellOffers());
        String newMarketOrders = String.valueOf(item.getBuyOrders());
        String marketSellDiff  = simplePriceFormat.formatted((item.getMarketSell() - watcher.getMarketSell()), currency);
        String marketBuyDiff   = simplePriceFormat.formatted((item.getMarketBuy() - watcher.getMarketBuy()), currency);
        String marketOfferDiff = String.valueOf(item.getSellOffers() - watcher.getSellOffers());
        String marketOrderDiff = String.valueOf(item.getBuyOrders() - watcher.getBuyOrders());

        this.appendDescription(switch (watcher.getTrigger()) {
            case SELL_UNDER, SELL_OVER, BUY_OVER, BUY_UNDER -> String.format(context.getTranslation(watcher.getTrigger()
                                                                                                           .getTranslationKey()), item.getName(), watcher.getPriceReference(), new TimeConverter(watcher.getTiming()));
            case EVERYTIME -> String.format(context.getTranslation(watcher.getTrigger()
                                                                          .getTranslationKey()), item.getName(), new TimeConverter(watcher.getTiming()));
        });

        String priceFormat = "%s\n%s %s";

        String sellEmote  = emoji.with(item.getMarketSell(), watcher.getMarketSell(), false);
        String buyEmote   = emoji.with(item.getMarketBuy(), watcher.getMarketBuy(), true);
        String offerEmote = emoji.with(item.getSellOffers(), watcher.getSellOffers(), false);
        String orderEmote = emoji.with(item.getBuyOrders(), watcher.getBuyOrders(), true);

        this.addField(context.getTranslation(TR_MARKET__SELL), String.format(priceFormat, newMarketSell, sellEmote, marketSellDiff), true);
        this.addField(context.getTranslation(TR_MARKET__BUY), String.format(priceFormat, newMarketBuy, buyEmote, marketBuyDiff), true);

        this.addBlankField(true);

        this.addField(context.getTranslation(TR_MARKET__SELL_OFFERS), String.format(priceFormat, newMarketOffers, offerEmote, marketOfferDiff), true);
        this.addField(context.getTranslation(TR_MARKET__BUY_ORDERS), String.format(priceFormat, newMarketOrders, orderEmote, marketOrderDiff), true);

        this.setImage(Utilities.createChartUrl(configuration, item, 5));
    }

}
