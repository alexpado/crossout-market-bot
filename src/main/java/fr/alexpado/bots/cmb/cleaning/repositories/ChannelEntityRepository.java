package fr.alexpado.bots.cmb.cleaning.repositories;

import fr.alexpado.bots.cmb.cleaning.entities.discord.ChannelEntity;
import fr.alexpado.bots.cmb.cleaning.entities.discord.GuildEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChannelEntityRepository extends JpaRepository<ChannelEntity, Long> {

    Optional<ChannelEntity> findByIdAndGuild(Long id, GuildEntity guild);

}
