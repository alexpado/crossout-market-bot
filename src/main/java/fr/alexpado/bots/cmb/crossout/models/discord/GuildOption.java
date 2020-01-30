package fr.alexpado.bots.cmb.crossout.models.discord;

import fr.alexpado.bots.cmb.crossout.models.composite.GuildOptionKey;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@IdClass(GuildOptionKey.class)
@Setter
public class GuildOption {

    @Id
    @OneToOne
    private DiscordGuild guild;

    @Id
    @Column(length = 100)
    private String optionKey;

    private String value;

}
