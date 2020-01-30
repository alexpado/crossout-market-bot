package fr.alexpado.bots.cmb.crossout.models.composite;

import fr.alexpado.bots.cmb.crossout.models.discord.DiscordGuild;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GuildOptionKey implements Serializable {

    public GuildOptionKey(DiscordGuild guild, String optionKey) {
        this.guild = guild;
        this.optionKey = optionKey;
    }

    private DiscordGuild guild;
    private String optionKey;

}
