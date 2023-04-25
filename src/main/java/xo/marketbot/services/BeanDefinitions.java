package xo.marketbot.services;

import fr.alexpado.xodb4j.XoDB;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import xo.marketbot.helpers.CrossoutCache;
import xo.marketbot.repositories.LanguageRepository;

@Component
public class BeanDefinitions {

    private static final Logger LOGGER = LoggerFactory.getLogger(BeanDefinitions.class);

    @Bean
    public CrossoutCache getItemProvider(LanguageRepository languageRepository, XoDB xoDB) throws Exception {

        return new CrossoutCache(xoDB, languageRepository.findAllByApiLanguageTrue());
    }

    @Bean
    public XoDB getXoDB() {

        return new XoDB();
    }

}
