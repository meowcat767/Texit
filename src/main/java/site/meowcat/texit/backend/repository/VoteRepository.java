package site.meowcat.texit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.meowcat.texit.backend.model.Post;
import site.meowcat.texit.backend.model.User;
import site.meowcat.texit.backend.model.Vote;

import java.util.Optional;

public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByPostAndUser(Post post, User user);

    void deleteByPost(Post post);
    boolean existsByUsername(String username);
}
