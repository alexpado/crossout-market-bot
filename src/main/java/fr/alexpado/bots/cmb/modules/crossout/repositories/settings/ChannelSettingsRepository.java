package fr.alexpado.bots.cmb.modules.crossout.repositories.settings;

import fr.alexpado.bots.cmb.modules.crossout.models.settings.ChannelSettings;
import fr.alexpado.bots.cmb.modules.discord.models.DiscordChannel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChannelSettingsRepository extends JpaRepository<ChannelSettings, Long> {

    Optional<ChannelSettings> findByChannel(DiscordChannel channel);

}
