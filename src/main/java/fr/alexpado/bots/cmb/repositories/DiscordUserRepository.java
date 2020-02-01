package fr.alexpado.bots.cmb.repositories;

import fr.alexpado.bots.cmb.models.discord.DiscordUser;
import org.springframework.data.repository.CrudRepository;

public interface DiscordUserRepository extends CrudRepository<DiscordUser, Long> {
}