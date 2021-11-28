package fr.alexpado.bots.cmb.interfaces.translatable;


import fr.alexpado.bots.cmb.CrossoutConfiguration;
import fr.alexpado.bots.cmb.modules.crossout.models.Translation;
import fr.alexpado.bots.cmb.modules.crossout.repositories.TranslationRepository;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class Translatable implements ITranslatable {

    private final CrossoutConfiguration config;
    private final TranslationRepository repository;
    private final Map<String, String>   translations = new HashMap<>();

    /**
     * Translatable constructor.
     *
     * @param config
     *         Application configuration holder
     */
    public Translatable(CrossoutConfiguration config) {

        this.config     = config;
        this.repository = config.getRepositoryAccessor().getTranslationRepository();
    }

    /**
     * Get the translated text corresponding to the provided key. Return a placeholder if the translation key couldn't
     * be found it the translation map.
     *
     * @param key
     *         Translation key to retrieve.
     *
     * @return Translated text corresponding to the key or a default placeholder.
     */
    @Override
    public String getTranslation(String key) {

        return this.translations.getOrDefault(key, String.format("[ERR:%s not found]", key));
    }

    /**
     * Load the required translations in the provided language from the database. Will try to fetch the missing ones
     * using the default app language defined in the configuration.
     *
     * @param language
     *         ISO-639-1 language compliant string.
     *
     * @throws MissingTranslationException
     *         Thrown when at least one translation key couldn't be loaded in the provided language, nor in the default
     *         app language.
     */
    @Override
    public void fetchTranslations(String language) throws MissingTranslationException {
        // Load the translation keys list to avoid multiple array generation.
        List<String> translationKeys = this.getRequiredTranslation();

        if (translationKeys.size() == 0) {
            return;
        }

        List<Translation> localeTranslations;
        try {
            localeTranslations = this.fetch(translationKeys, language);
        } catch (MissingTranslationException e) {
            // Try to fetch missing keys with the default language.
            localeTranslations = this.fetch(e.getMissingTranslations(), this.config.getDefaultLocale());
            // Add already loaded translation in the list.
            localeTranslations.addAll(e.getLoadedTranslations());
        }
        // Fill the translation map.
        localeTranslations.forEach(translation -> this.translations.put(translation.getTranslationKey(), translation.getText()));
    }

    /**
     * Fetch the translation keys in the language provided.
     *
     * @param keys
     *         List of translation keys to retrieve
     * @param language
     *         Language to use when retrieving the translations
     *
     * @return A list of all requested translations
     *
     * @throws MissingTranslationException
     *         Thrown when at least one translation couldn't be retrieved. Contains a list of missing translation keys
     *         and a list of loaded translations in the language provided.
     */
    @Override
    public List<Translation> fetch(List<String> keys, String language) throws MissingTranslationException {
        // Fetch the translations from the database.
        List<Translation> translationList = this.repository.getNeededFromLanguage(keys, language);
        // Is there any translations missing ?
        if (translationList.size() != keys.size()) {
            // Prepare the list of keys that has been retrieved.
            List<String> retrievedKeys = translationList.stream()
                                                        .map(Translation::getTranslationKey)
                                                        .collect(Collectors.toList());
            // Get the missing keys
            List<String> missingKeys = keys.stream()
                                           .filter(key -> !retrievedKeys.contains(key))
                                           .collect(Collectors.toList());
            // Throw the exception.
            throw new MissingTranslationException(missingKeys, translationList);
        }
        return translationList;
    }

}
