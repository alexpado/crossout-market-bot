package fr.alexpado.bots.cmb.interfaces.translatable;


import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.throwables.MissingTranslationException;

import java.util.List;

public interface ITranslatable {

    /**
     * Defines all the translation keys to fetch from the database when calling {@link #fetchTranslations(String)}.
     *
     * @return A list of translation keys to fetch.
     */
    List<String> getRequiredTranslation();

    /**
     * Get the translated text corresponding to the provided key. Return a placeholder if the translation key couldn't
     * be found it the translation map.
     *
     * @param key
     *         Translation key to retrieve.
     *
     * @return Translated text corresponding to the key or a default placeholder.
     */
    String getTranslation(String key);

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
    void fetchTranslations(String language) throws MissingTranslationException;

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
    List<Translation> fetch(List<String> keys, String language) throws MissingTranslationException;

}