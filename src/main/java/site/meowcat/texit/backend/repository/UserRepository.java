package site.meowcat.texit.backend.repository;

import site.meowcat.texit.backend.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
}
