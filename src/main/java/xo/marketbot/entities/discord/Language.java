package xo.marketbot.entities.discord;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Language {

    @Id
    private String        id;
    private String        name;
    private String        localized;
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
