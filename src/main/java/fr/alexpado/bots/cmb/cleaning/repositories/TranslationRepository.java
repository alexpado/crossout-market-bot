package fr.alexpado.bots.cmb.cleaning.repositories;

import fr.alexpado.bots.cmb.cleaning.entities.i18n.Translation;
import fr.alexpado.bots.cmb.cleaning.repositories.keys.TranslationKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, TranslationKey> {}
