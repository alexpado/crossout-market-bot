package xo.marketbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xo.marketbot.entities.discord.Language;
import xo.marketbot.entities.discord.Translation;
import xo.marketbot.repositories.keys.TranslationKey;

import java.util.List;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, TranslationKey> {

    List<Translation> findAllByLanguage(Language language);

}
