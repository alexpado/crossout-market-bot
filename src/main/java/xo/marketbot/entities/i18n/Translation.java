package xo.marketbot.entities.i18n;

import fr.alexpado.jda.services.translations.interfaces.ITranslation;
import org.jetbrains.annotations.NotNull;
import xo.marketbot.repositories.keys.TranslationKey;

import javax.persistence.*;

@Entity
@IdClass(TranslationKey.class)
public class Translation implements ITranslation {

    @Id
    private String key;

    @Id
    @Column(length = 3)
    private String language;

    @Lob
    private String value;

    /**
     * Get the key of this translation.
     *
     * @return The translation's key
     */
    @Override
    public @NotNull String getKey() {

        return this.key;
    }

    /**
     * Get the language of this translation.
     *
     * @return The translation's language.
     */
    @Override
    public @NotNull String getLanguage() {

        return this.language;
    }

    /**
     * Get the translation string value.
     *
     * @return The string value to apply to the annotated field with the {@link #getKey()} string.
     */
    @Override
    public @NotNull String getValue() {

        return this.value;
    }
}
