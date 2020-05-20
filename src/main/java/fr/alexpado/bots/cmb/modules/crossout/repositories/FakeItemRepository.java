package fr.alexpado.bots.cmb.modules.crossout.repositories;

import fr.alexpado.bots.cmb.modules.crossout.models.FakeItem;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FakeItemRepository extends CrudRepository<FakeItem, Integer> {

    @Query("SELECT i FROM FakeItem i WHERE LOWER(i.name) LIKE :name")
    Optional<FakeItem> findEasterEgg(@Param("name") String itemName);

}
