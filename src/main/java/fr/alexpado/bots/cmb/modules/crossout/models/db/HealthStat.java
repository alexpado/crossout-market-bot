package fr.alexpado.bots.cmb.modules.crossout.models.db;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class HealthStat {

    @Id
    private LocalDateTime runAt;
    private long          responseTime;

    public HealthStat() {
        // No arg constructor for SpringBoot
    }

    private HealthStat(long responseTime) {

        this.responseTime = responseTime;
        this.runAt        = LocalDateTime.now();
    }

    public static HealthStat create(long responseTime) {

        return new HealthStat(responseTime);
    }

    public boolean isAvailable() {

        return this.responseTime != -1;
    }

}
