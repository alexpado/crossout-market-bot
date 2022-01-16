package xo.marketbot.repositories.keys;

import java.io.Serializable;
import java.util.Objects;

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

        if (this == o) {return true;}
        if (!(o instanceof TranslationKey that)) {return false;}
        return key.equals(that.key) && language.equals(that.language);
    }

    @Override
    public int hashCode() {

        return Objects.hash(key, language);
    }

    public String getKey() {

        return key;
    }

    public void setKey(String key) {

        this.key = key;
    }

    public String getLanguage() {

        return language;
    }

    public void setLanguage(String language) {

        this.language = language;
    }
}
