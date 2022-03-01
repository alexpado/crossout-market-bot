package xo.marketbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xo.marketbot.entities.discord.Watcher;

import java.util.List;

@Repository
public interface WatcherRepository extends JpaRepository<Watcher, Integer> {

    @Query("SELECT w FROM Watcher w WHERE w.owner.watcherPaused = false AND w.failureCount < 3 ORDER BY w.lastExecution ASC")
    List<Watcher> findAllPotentials();

    List<Watcher> findAllByOwnerId(long owner_id);

}
