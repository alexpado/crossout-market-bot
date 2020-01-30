package fr.alexpado.bots.cmb.crossout.repositories;

import fr.alexpado.bots.cmb.crossout.models.discord.DiscordUser;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.data.repository.CrudRepository;

public interface DiscordUserRepository extends CrudRepository<DiscordUser, Long> {
}