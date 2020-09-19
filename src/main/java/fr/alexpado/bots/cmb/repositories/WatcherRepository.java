package fr.alexpado.bots.cmb.repositories;

import fr.alexpado.bots.cmb.entities.discord.Watcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatcherRepository extends JpaRepository<Watcher, Integer> {

    @Query("SELECT w FROM Watcher w WHERE w.user.watcherPaused = false")
    List<Watcher> getAllPotentials();

}
