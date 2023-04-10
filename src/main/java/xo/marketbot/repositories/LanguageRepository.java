package xo.marketbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xo.marketbot.entities.discord.Language;

import java.util.List;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {

    List<Language> findAllByApiLanguageTrue();

}
