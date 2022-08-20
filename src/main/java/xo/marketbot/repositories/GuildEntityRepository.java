package xo.marketbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xo.marketbot.entities.discord.GuildEntity;

@Repository
public interface GuildEntityRepository extends JpaRepository<GuildEntity, Long> {}
