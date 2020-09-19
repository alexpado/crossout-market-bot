package xo.marketbot.interfaces.common;

/**
 * Interface representing an object holding a language setting.
 *
 * @author alexpado
 */
public interface LanguageHolder {

    /**
     * Retrieve this {@link LanguageHolder}'s language. The language is usually represented by two characters.
     *
     * @return This {@link LanguageHolder}'s language.
     */
    String getLanguage();

    /**
     * Define this {@link LanguageHolder}'s language. The language is usually represented by two characters.
     *
     * @param language
     *         This {@link LanguageHolder}'s language.
     */
    void setLanguage(String language);

}
