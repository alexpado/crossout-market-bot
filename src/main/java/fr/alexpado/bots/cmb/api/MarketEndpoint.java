package fr.alexpado.bots.cmb.api;

import fr.alexpado.bots.cmb.libs.HttpRequest;
import fr.alexpado.bots.cmb.tools.graph.GraphSet;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MarketEndpoint {

    private String host;

    /**
     * APIEndpoint constructor.
     *
     * @param host
     *         API Host (root) to use for this endpoint.
     */
    public MarketEndpoint(String host) {
        this.host = host;
    }

    public List<GraphSet> getMarketData(long start, long end, int itemID) throws Exception {
        HttpRequest request = new HttpRequest(String.format("%s/market-all/%s?startTimestamp=%s&endTimestamp=%s", this.host, itemID, start, end));
        JSONArray array = request.readJsonArray();

        List<Integer> buyPrices = new ArrayList<>();
        List<Integer> sellPrices = new ArrayList<>();

        if (array.length() < 2) {
            throw new IllegalStateException("Not enough data for a graph !");
        }

        for (int i = 0; i < array.length(); i++) {
            JSONArray market = array.getJSONArray(i);

            sellPrices.add(market.getInt(1));
            buyPrices.add(market.getInt(2));
        }

        return Arrays.asList(new GraphSet(buyPrices, "Sell"), new GraphSet(sellPrices, "Buy"));
    }


}
