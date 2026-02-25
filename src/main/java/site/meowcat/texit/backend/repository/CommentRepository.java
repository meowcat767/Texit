package site.meowcat.texit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.meowcat.texit.backend.model.Comment;
import site.meowcat.texit.backend.model.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByPostOrderByCreatedAtDesc(Post post);
}
