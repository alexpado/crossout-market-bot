package fr.alexpado.bots.cmb.crossout.repositories;

import fr.alexpado.bots.cmb.crossout.models.composite.GuildOptionKey;
import fr.alexpado.bots.cmb.crossout.models.discord.DiscordGuild;
import fr.alexpado.bots.cmb.crossout.models.discord.GuildOption;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface GuildOptionRepository extends CrudRepository<GuildOption, GuildOptionKey> {

    @Query("SELECT o.value FROM GuildOption o WHERE o.guild = :guild AND o.optionKey = :option")
    Optional<String> findOption(@Param("guild") DiscordGuild guild, @Param("option") String optionKey);

}