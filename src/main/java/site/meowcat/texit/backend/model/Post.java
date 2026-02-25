package site.meowcat.texit.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Table;
import site.meowcat.texit.backend.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue
    private Long id;

    private String title;

    @Column(length = 5000)
    private String body;

    private int votes = 0;
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    private User author;

    public Post() { }

    public Post(String title, String body, User author) {
        this.title = title;
        this.body = body;
        this.author = author;
    }
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getBody() { return body; }
    public void setBody(String body) { this.body = body; }
    public int getVotes() { return votes; }
    public void setVotes(int votes) { this.votes = votes; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public User getAuthor() { return author; }
    public void setAuthor(User author) { this.author = author; }
}