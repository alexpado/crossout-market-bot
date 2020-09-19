package xo.marketbot.web.auth.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import xo.marketbot.web.auth.entities.Session;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {

    @Query("SELECT s FROM Session s WHERE s.expiresAt > :expirationLimit")
    List<Session> findRefreshable(LocalDateTime expirationLimit);

}
