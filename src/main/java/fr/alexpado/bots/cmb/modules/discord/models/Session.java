package fr.alexpado.bots.cmb.modules.discord.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Objects;

@Entity
@Getter
@Setter
public class Session {

    @Id
    @Column(length = 64)
    private String code;

    @OneToOne
    private DiscordUser discordUser;

    private String ipAddress;

    private Long createdAt = System.currentTimeMillis();

    private Long lastUse = System.currentTimeMillis();

    @Override
    public boolean equals(Object o) {

        if (this == o) { return true; }
        if (!(o instanceof Session)) { return false; }
        Session session = (Session) o;
        return this.code.equals(session.code);
    }

    @Override
    public int hashCode() {

        return Objects.hash(this.code);
    }

}
