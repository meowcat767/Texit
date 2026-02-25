package site.meowcat.texit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.meowcat.texit.backend.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
