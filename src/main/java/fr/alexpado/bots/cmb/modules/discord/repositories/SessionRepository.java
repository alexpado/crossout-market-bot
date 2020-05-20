package fr.alexpado.bots.cmb.modules.discord.repositories;


import fr.alexpado.bots.cmb.modules.discord.models.DiscordUser;
import fr.alexpado.bots.cmb.modules.discord.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, Integer> {

    List<Session> findAllByDiscordUser(DiscordUser discordUser);

    Optional<Session> findByCode(String code);

    Optional<Session> findByCodeAndDiscordUser(String code, DiscordUser discordUser);

}
