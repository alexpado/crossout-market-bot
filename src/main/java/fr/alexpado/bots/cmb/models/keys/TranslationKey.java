package fr.alexpado.bots.cmb.models.keys;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TranslationKey implements Serializable {

    public static TranslationKey of(String language, String translationKey) {
        TranslationKey key = new TranslationKey();
        key.language = language;
        key.translationKey = translationKey;
        return key;
    }

    private String translationKey;
    private String language;

}
