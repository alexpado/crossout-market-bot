package fr.alexpado.bots.cmb.modules.crossout.repositories.settings;

import fr.alexpado.bots.cmb.modules.crossout.models.settings.GuildSettings;
import fr.alexpado.bots.cmb.modules.discord.models.DiscordGuild;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GuildSettingsRepository extends JpaRepository<GuildSettings, Long> {

    Optional<GuildSettings> findByGuild(DiscordGuild guild);

}
