package uz.nukuslab.dogovor.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.nukuslab.dogovor.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

}
