package xo.marketbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xo.marketbot.entities.discord.CachedItem;

@Repository
public interface CachedItemRepository extends JpaRepository<CachedItem, Integer> {

}
