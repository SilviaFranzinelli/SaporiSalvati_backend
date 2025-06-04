package it.epicode.SaporiSalvati.repository;



import it.epicode.SaporiSalvati.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
}