package site.meowcat.texit.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import site.meowcat.texit.backend.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> { }
