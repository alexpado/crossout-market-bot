package fr.alexpado.bots.cmb.modules.crossout.controllers;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.api.MarketEndpoint;
import fr.alexpado.bots.cmb.tools.graph.GraphSet;
import fr.alexpado.bots.cmb.tools.graph.MarketGraph;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

@RestController()
public class ChartController {

    private final CrossoutConfiguration crossoutConfiguration;

    public ChartController(@Qualifier("crossoutConfiguration") CrossoutConfiguration config) {
        this.crossoutConfiguration = config;
    }

    @GetMapping(value = "/chart/{end:[0-9]*}/{itemID:[0-9]*}.png", produces = {MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody
    byte[] getChartPicture(@PathVariable long end, @PathVariable int itemID) throws Exception {

        try {
            long interval = crossoutConfiguration.getGraphInterval();

            MarketEndpoint market = new MarketEndpoint(crossoutConfiguration.getApiHost());
            List<GraphSet> graphs = market.getMarketData(end - interval, end, itemID);

            MarketGraph graph = new MarketGraph("Last 5 hours", graphs.get(0), graphs.get(1));
            BufferedImage img = graph.draw(Color.GREEN, Color.RED);

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(img, "png", output);
            return output.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            BufferedImage img = ImageIO.read(new File("graph3fail.png"));
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            ImageIO.write(img, "png", output);
            return output.toByteArray();
        }
    }

}
