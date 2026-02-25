package site.meowcat.texit.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments")
public class Comment {
    @Id @GeneratedValue
    private long id;
    private String body;
    private LocalDateTime createdAt = LocalDateTime.now();
    @ManyToOne
    private User author;
    @ManyToOne
    private Post post;

    public Comment() {}

    public Comment(String body, User author, Post post) {
        this.body = body;
        this.author = author;
        this.post = post;
    }

    public long getId() { return id; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
}
