package xo.marketbot.services;

import fr.alexpado.xodb4j.XoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanDefinitions.class);

    @Bean
    public XoDB getXoDB() throws Exception {

        LOGGER.info("Building XODB cache...");
        XoDB xoDB = new XoDB();
        xoDB.buildCaches(true);
        LOGGER.info("Cache built");
        return xoDB;
    }

}
