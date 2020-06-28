package fr.alexpado.bots.cmb.modules.crossout.repositories.db;

import fr.alexpado.bots.cmb.modules.crossout.models.db.HealthStat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface HealthStatRepository extends JpaRepository<HealthStat, LocalDateTime> {

    void deleteAllByRunAtBefore(LocalDateTime runAt);

    void getAllByRunAtAfter(LocalDateTime runAt);

}
