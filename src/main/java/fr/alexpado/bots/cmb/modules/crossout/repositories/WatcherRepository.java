package fr.alexpado.bots.cmb.modules.crossout.repositories;

import fr.alexpado.bots.cmb.modules.crossout.models.Watcher;
import fr.alexpado.bots.cmb.modules.discord.models.DiscordUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface WatcherRepository extends CrudRepository<Watcher, Integer> {

    @Query(value = "SELECT w FROM Watcher w WHERE w.lastExecution + w.repeatEvery < :min AND w.user.id in (SELECT us.id FROM UserSettings us WHERE us.id = w.user.id AND us.watcherPaused = false)")
    List<Watcher> getExecutables(@Param("min") long minExecutionTime);

    List<Watcher> findAllByUser(DiscordUser user);

    List<Watcher> findAllByUserInAndLastExecutionIsGreaterThan(Collection<DiscordUser> user, long lastExecution);

}