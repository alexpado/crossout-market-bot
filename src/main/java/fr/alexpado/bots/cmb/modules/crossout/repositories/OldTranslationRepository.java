package fr.alexpado.bots.cmb.modules.crossout.repositories;

import fr.alexpado.bots.cmb.modules.crossout.models.OldTranslation;
import fr.alexpado.bots.cmb.modules.crossout.models.keys.TranslationKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface OldTranslationRepository extends CrudRepository<OldTranslation, TranslationKey> {

    @Query(value = "SELECT t FROM OldTranslation t WHERE t.translationKey IN :keys AND t.language = :lang")
    List<OldTranslation> getNeededFromLanguage(@Param("keys") Collection<String> keys, @Param("lang") String language);

    Optional<OldTranslation> getTranslationByLanguageAndTranslationKey(String language, String translationKey);

    @Query("SELECT DISTINCT t.language FROM OldTranslation t")
    List<String> supportedLanguages();

}
