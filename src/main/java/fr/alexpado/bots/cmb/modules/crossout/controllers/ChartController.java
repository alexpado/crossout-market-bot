package fr.alexpado.bots.cmb.modules.crossout.controllers;

import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.api.MarketEndpoint;
import fr.alexpado.bots.cmb.tools.graph.GraphSet;
import fr.alexpado.bots.cmb.tools.graph.MarketGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.io.FileOutputStream;
import java.util.List;

@RestController()
public class ChartController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChartController.class);

    private final CrossoutConfiguration crossoutConfiguration;

    public ChartController(@Qualifier("crossoutConfiguration") CrossoutConfiguration config) {

        this.crossoutConfiguration = config;
    }

    private File getGraph(String hashIdentifier) throws Exception {

        File cacheFolder = new File("cache");

        if (!cacheFolder.exists()) {
            if (!cacheFolder.mkdirs()) {
                throw new Exception("Unable to create the cache directory.");
            }
        }

        File graphCacheFolder = new File(cacheFolder, "graph");

        if (!graphCacheFolder.exists()) {
            if (!graphCacheFolder.mkdirs()) {
                throw new Exception("Unable to create the graph cache directory.");
            }
        }

        return new File(graphCacheFolder, hashIdentifier);
    }

    @GetMapping(value = "/chart/{end:[0-9]*}/{itemID:[0-9]*}.png", produces = {MediaType.IMAGE_PNG_VALUE})
    public @ResponseBody
    byte[] getChartPicture(@PathVariable long end, @PathVariable int itemID) throws Exception {
        try {
            String        imgIdentifier = end + "$" + itemID;
            File          graphFile     = this.getGraph(imgIdentifier);
            BufferedImage image;

            if (!this.crossoutConfiguration.isCacheEnabled()) {
                LOGGER.warn("The chart cache is disabled !");
            }

            if (this.crossoutConfiguration.isCacheEnabled() && graphFile.exists()) {
                image = ImageIO.read(graphFile);
            } else {
                long interval = crossoutConfiguration.getGraphInterval();

                MarketEndpoint market = new MarketEndpoint(crossoutConfiguration.getApiHost());
                List<GraphSet> graphs = market.getMarketData(end - interval, end, itemID);

                MarketGraph graph = new MarketGraph("Last 5 hours", graphs.get(0), graphs.get(1));
                image = graph.draw(Color.GREEN, Color.RED);
            }

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(graphFile);

            ImageIO.write(image, "png", output);

            ImageIO.write(image, "png", fileOutputStream);
            fileOutputStream.close();

            output.close();
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
