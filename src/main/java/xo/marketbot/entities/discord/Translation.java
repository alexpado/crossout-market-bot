package xo.marketbot.entities.discord;

import xo.marketbot.repositories.keys.TranslationKey;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.Objects;

@IdClass(TranslationKey.class)
@Entity
public class Translation {

    @Id
    private String        key;
    @Id
    @ManyToOne
    private Language      language;
    private String        value;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Translation() {

    }

    public Translation(String key, Language language, String value) {

        this.key      = key;
        this.language = language;
        this.value    = value;
    }

    /**
     * Retrieve this {@link Translation}'s key.
     *
     * @return The key.
     */
    public String getKey() {

        return this.key;
    }

    /**
     * Retrieve this {@link Translation}'s {@link Language}.
     *
     * @return The {@link Language}.
     */
    public Language getLanguage() {

        return this.language;
    }

    /**
     * Retrieve this {@link Translation}'s value.
     *
     * @return The value.
     */
    public String getValue() {

        return this.value;
    }

    /**
     * Retrieve this {@link Translation} creation date.
     *
     * @return The creation date.
     */
    public LocalDateTime getCreatedAt() {

        return this.createdAt;
    }

    /**
     * Retrieve this {@link Translation}'s updated date.
     *
     * @return The updated date.
     */
    public LocalDateTime getUpdatedAt() {

        return this.updatedAt;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Translation translation) {
            return this.getKey().equals(translation.getKey()) && this.getLanguage().equals(translation.getLanguage());
        }
        return false;
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.getKey(), this.getLanguage());
    }

}
