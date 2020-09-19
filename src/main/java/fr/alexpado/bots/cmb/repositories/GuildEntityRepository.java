package fr.alexpado.bots.cmb.repositories;

import fr.alexpado.bots.cmb.entities.discord.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildEntityRepository extends JpaRepository<GuildEntity, Long> {}
