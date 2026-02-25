package site.meowcat.texit.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;

@Entity
public class Comment {
    @Id @GeneratedValue
    private long id;
    private String body;
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne
    private User author;
    @ManyToOne
    private Post post;
}
