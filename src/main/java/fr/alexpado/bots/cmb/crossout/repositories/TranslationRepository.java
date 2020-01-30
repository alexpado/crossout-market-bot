package fr.alexpado.bots.cmb.crossout.repositories;

import fr.alexpado.bots.cmb.crossout.models.Translation;
import fr.alexpado.bots.cmb.crossout.models.composite.TranslationKey;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TranslationRepository extends CrudRepository<Translation, TranslationKey> {

    @Query(value = "SELECT t FROM Translation t WHERE t.translationKey IN :keys AND t.language = :lang")
    List<Translation> getNeededFromLanguage(@Param("keys") List<String> keys, @Param("lang") String language);

}
