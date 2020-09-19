package xo.marketbot.repositories.keys;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class TranslationKey implements Serializable {

    private String key;
    private String language;

    public static TranslationKey of(String language, String translationKey) {

        TranslationKey key = new TranslationKey();
        key.language = language;
        key.key      = translationKey;
        return key;
    }

}
