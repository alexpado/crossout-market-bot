package xo.marketbot.entities.discord;

import fr.alexpado.xodb4j.XoDB;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Language {

    @Id
    private String        id;
    private String        name;
    private String        localized;
    private boolean       apiLanguage;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Retrieve this {@link Language}'s ID.
     *
     * @return The ID.
     */
    public String getId() {

        return this.id;
    }

    /**
     * Retrieve this {@link Language} english name
     *
     * @return The english name.
     */
    public String getName() {

        return this.name;
    }

    /**
     * Retrieve this {@link Language} name.
     *
     * @return The name.
     */
    public String getLocalized() {

        return this.localized;
    }

    /**
     * Check if this {@link Language} can be used to query CrossoutDB API through {@link XoDB} to get localized
     * content.
     *
     * @return True if usable with the api, false otherwise.
     */
    public boolean isApiLanguage() {

        return apiLanguage;
    }

    /**
     * Retrieve this {@link Language} creation date.
     *
     * @return The creation date.
     */
    public LocalDateTime getCreatedAt() {

        return this.createdAt;
    }

    /**
     * Retrieve this {@link Language}'s updated date.
     *
     * @return The updated date.
     */
    public LocalDateTime getUpdatedAt() {

        return this.updatedAt;
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof Language language) {
            return this.getId().equals(language.getId());
        }
        return false;
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.getId());
    }

}
