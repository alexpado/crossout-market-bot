package xo.marketbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xo.marketbot.entities.discord.ChannelEntity;
import xo.marketbot.entities.discord.GuildEntity;

import java.util.Optional;

@Repository
public interface ChannelEntityRepository extends JpaRepository<ChannelEntity, Long> {

    Optional<ChannelEntity> findByIdAndGuild(Long id, GuildEntity guild);

}
