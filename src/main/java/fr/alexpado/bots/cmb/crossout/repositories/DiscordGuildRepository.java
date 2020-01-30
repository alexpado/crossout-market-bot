package fr.alexpado.bots.cmb.crossout.repositories;

import fr.alexpado.bots.cmb.crossout.models.discord.DiscordGuild;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.data.repository.CrudRepository;

public interface DiscordGuildRepository extends CrudRepository<DiscordGuild, Long> {
}