package site.meowcat.texit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.meowcat.texit.backend.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> { }
