package xo.marketbot.services;

import fr.alexpado.xodb4j.XoDB;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class BeanDefinitions {

    @Bean
    public XoDB getXoDB() throws Exception {

        return new XoDB();
    }

}
