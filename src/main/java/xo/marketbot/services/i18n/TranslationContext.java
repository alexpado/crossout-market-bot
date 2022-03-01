package xo.marketbot.services.i18n;

import java.util.Map;

public class TranslationContext {

    private final Map<String, String> translations;

    public TranslationContext(Map<String, String> translations) {

        this.translations = translations;
    }

    public String getTranslation(String key) {

        return this.translations.getOrDefault(key, String.format("[[ missing %s ]]", key));
    }

}
