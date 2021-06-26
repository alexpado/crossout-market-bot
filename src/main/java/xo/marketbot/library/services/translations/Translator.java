package xo.marketbot.library.services.translations;


import io.sentry.Sentry;
import io.sentry.SentryLevel;
import xo.marketbot.library.services.translations.annotations.I18N;
import xo.marketbot.library.services.translations.interfaces.ITranslationProvider;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Translator {

    public static void translate(ITranslationProvider provider, String language, Object target) throws IllegalAccessException {

        List<String> missing = new ArrayList<>();
        List<String> all     = new ArrayList<>();

        for (Field declaredField : target.getClass().getDeclaredFields()) {

            I18N i18n = declaredField.getAnnotation(I18N.class);

            if (i18n == null) {
                continue;
            }

            if (!i18n.value().isEmpty() && declaredField.getType() == String.class) {
                // Direct translation
                String translation = provider.getTranslation(language, i18n.value());

                all.add(i18n.value());
                if (translation == null) {
                    missing.add(i18n.value());
                    translation = String.format("[[ '%s' missing in '%s' ]]", i18n.value(), language);
                }

                if (!declaredField.canAccess(target)) {
                    declaredField.setAccessible(true);
                    declaredField.set(target, translation);
                    declaredField.setAccessible(false);
                } else {
                    declaredField.set(target, translation);
                }

            } else if (i18n.value().isEmpty()) {
                // Indirect translation
                if (!declaredField.canAccess(target)) {
                    declaredField.setAccessible(true);
                    translate(provider, language, declaredField.get(target));
                    declaredField.setAccessible(false);
                } else {
                    translate(provider, language, declaredField.get(target));
                }
            }

        }

        if (!missing.isEmpty()) {
            Sentry.configureScope(scope -> {
                scope.setLevel(SentryLevel.WARNING);

                scope.setTag("language", language);
                scope.setTag("class", target.getClass().getSimpleName());

                Map<String, String> missingFields = new LinkedHashMap<>();

                all.forEach(item -> missingFields.put(item, missing.contains(item) ? "Missing" : "Not missing"));

                scope.setContexts("Translations", missingFields);
            });

            Sentry.captureMessage("Some translations are missing.");
        }
    }

}
