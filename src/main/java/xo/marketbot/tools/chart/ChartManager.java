package xo.marketbot.tools.chart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import xo.marketbot.configurations.interfaces.IChartConfiguration;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

@Service
public class ChartManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChartManager.class);

    private final IChartConfiguration configuration;
    private final BufferedImage       image;

    public ChartManager(IChartConfiguration configuration) {

        this.configuration = configuration;

        File baseImage = new File(this.configuration.getSource());

        if (!baseImage.exists() || !baseImage.isFile()) {
            LOGGER.error("Initialize state failure: Source not found.");
            throw new IllegalStateException("Unable to load the base image: The path provided doesn't exist or is not a file.");
        }

        try {
            this.image = ImageIO.read(baseImage);
        } catch (IOException e) {
            LOGGER.error("Initialize state failure: Unable to open source.", e);
            throw new IllegalStateException("Unable to create the ChartManager service.");
        }
    }

    private BufferedImage getImageCopy() {

        ColorModel     cm                   = this.image.getColorModel();
        boolean        isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster               = this.image.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public Chart createChart(String chartName) {

        return new Chart(this.configuration, this.getImageCopy(), chartName);
    }
}
