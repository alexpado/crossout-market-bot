package fr.alexpado.bots.cmb.repositories;

import fr.alexpado.bots.cmb.models.discord.DiscordChannel;
import org.springframework.data.repository.CrudRepository;

public interface DiscordChannelRepository extends CrudRepository<DiscordChannel, Long> {
}
