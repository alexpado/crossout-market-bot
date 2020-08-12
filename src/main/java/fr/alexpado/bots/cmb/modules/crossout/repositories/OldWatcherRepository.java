package fr.alexpado.bots.cmb.modules.crossout.repositories;

import fr.alexpado.bots.cmb.modules.crossout.models.Watcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OldWatcherRepository extends JpaRepository<Watcher, Integer> {

    @Query(value = "SELECT w FROM Watcher w WHERE w.lastExecution + w.repeatEvery < :min AND w.user.id in (SELECT us.id FROM UserSettings us WHERE us.id = w.user.id AND us.watcherPaused = false)")
    List<Watcher> getExecutables(@Param("min") long minExecutionTime);

    List<Watcher> findAllByUserId(Long user_id);

}