package xo.marketbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xo.marketbot.entities.discord.Language;

@Repository
public interface LanguageRepository extends JpaRepository<Language, String> {

}
