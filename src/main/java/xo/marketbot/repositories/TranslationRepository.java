package xo.marketbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xo.marketbot.entities.i18n.Translation;
import xo.marketbot.repositories.keys.TranslationKey;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, TranslationKey> {}
