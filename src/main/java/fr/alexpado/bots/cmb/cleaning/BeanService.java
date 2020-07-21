package fr.alexpado.bots.cmb.cleaning;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class BeanService {

    @Bean
    public XoDB xoDB(@Value("${api.host}") String rootUrl) {

        return new XoDB(rootUrl);
    }

}
