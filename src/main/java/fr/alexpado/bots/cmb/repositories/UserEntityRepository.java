package fr.alexpado.bots.cmb.repositories;

import fr.alexpado.bots.cmb.entities.discord.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {}
