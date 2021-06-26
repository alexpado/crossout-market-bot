package xo.marketbot.library.services.translations.interfaces;

/**
 * Interface representing a translation entry (key - language - value). This will be used as main
 */
public interface ITranslation {

    /**
     * Get the key of this translation.
     *
     * @return The translation's key
     */
    String getKey();

    /**
     * Get the language of this translation.
     *
     * @return The translation's language.
     */
    String getLanguage();

    /**
     * Get the translation string value.
     *
     * @return The string value to apply to the annotated field with the {@link #getKey()} string.
     */
    String getValue();

}
