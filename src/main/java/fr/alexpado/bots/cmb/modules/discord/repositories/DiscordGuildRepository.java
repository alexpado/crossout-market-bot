package fr.alexpado.bots.cmb.modules.discord.repositories;


import fr.alexpado.bots.cmb.modules.discord.models.DiscordGuild;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscordGuildRepository extends JpaRepository<DiscordGuild, Long> {
}
