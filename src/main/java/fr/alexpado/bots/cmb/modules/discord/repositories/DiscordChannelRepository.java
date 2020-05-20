package fr.alexpado.bots.cmb.modules.discord.repositories;


import fr.alexpado.bots.cmb.modules.discord.models.DiscordChannel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscordChannelRepository extends JpaRepository<DiscordChannel, Long> {
}
