package fr.alexpado.bots.cmb.crossout.repositories;

import fr.alexpado.bots.cmb.crossout.models.Watcher;
import fr.alexpado.bots.cmb.crossout.models.discord.DiscordUser;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WatcherRepository extends CrudRepository<Watcher, Integer> {

    @Query(value = "SELECT w FROM Watcher w WHERE w.lastExecution > :min")
    List<Watcher> getExecutables(@Param("min") long minExecutionTime);

    @Query(value = "SELECT w FROM Watcher w WHERE w.user = :user")
    List<Watcher> getFromUser(@Param("user") DiscordUser user);

}