package fr.alexpado.bots.cmb.repositories;

import fr.alexpado.bots.cmb.models.Translation;
import fr.alexpado.bots.cmb.models.keys.TranslationKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TranslationRepository extends CrudRepository<Translation, TranslationKey> {

    @Query(value = "SELECT t FROM Translation t WHERE t.translationKey IN :keys AND t.language = :lang")
    List<Translation> getNeededFromLanguage(@Param("keys") List<String> keys, @Param("lang") String language);

    Optional<Translation> getTranslationByLanguageAndTranslationKey(String language, String translationKey);
}
