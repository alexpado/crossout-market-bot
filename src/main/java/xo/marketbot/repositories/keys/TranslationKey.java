package xo.marketbot.repositories.keys;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {

        if (this == o) { return true; }
        if (!(o instanceof TranslationKey)) { return false; }
        TranslationKey that = (TranslationKey) o;
        return key.equals(that.key) && language.equals(that.language);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, language);
    }
}
