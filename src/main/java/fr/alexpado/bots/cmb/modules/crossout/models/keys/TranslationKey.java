package fr.alexpado.bots.cmb.modules.crossout.models.keys;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TranslationKey implements Serializable {

    private String translationKey;
    private String language;

    public static TranslationKey of(String language, String translationKey) {
        TranslationKey key = new TranslationKey();
        key.language = language;
        key.translationKey = translationKey;
        return key;
    }

}
