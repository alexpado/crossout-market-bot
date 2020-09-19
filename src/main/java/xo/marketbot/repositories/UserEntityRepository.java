package xo.marketbot.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xo.marketbot.entities.discord.User;

@Repository
public interface UserEntityRepository extends JpaRepository<User, Long> {}
