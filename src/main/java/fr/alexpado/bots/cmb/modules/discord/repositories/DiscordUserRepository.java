package fr.alexpado.bots.cmb.modules.discord.repositories;

import fr.alexpado.bots.cmb.modules.discord.models.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscordUserRepository extends JpaRepository<DiscordUser, Long> {
}
