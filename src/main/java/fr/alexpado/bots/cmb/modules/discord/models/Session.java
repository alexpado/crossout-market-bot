package fr.alexpado.bots.cmb.modules.discord.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;

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

}
