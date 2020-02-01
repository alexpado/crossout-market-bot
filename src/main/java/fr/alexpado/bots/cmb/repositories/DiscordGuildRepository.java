package fr.alexpado.bots.cmb.repositories;

import fr.alexpado.bots.cmb.models.discord.DiscordGuild;
import org.springframework.data.repository.CrudRepository;

public interface DiscordGuildRepository extends CrudRepository<DiscordGuild, Long> {
}