package fr.alexpado.bots.cmb.cleaning.repositories;

import fr.alexpado.bots.cmb.cleaning.entities.discord.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserEntityRepository extends JpaRepository<UserEntity, Long> {}
