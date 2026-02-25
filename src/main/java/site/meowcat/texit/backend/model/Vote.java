package site.meowcat.texit.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "votes")
public class Vote {
    @Id @GeneratedValue
    private Long id;
    private int value; // 1 = upvote, -1 = downvote
    @ManyToOne
    private User user;
    @ManyToOne
    private Post post;

    public Vote() {}

    public Vote(int value, User user, Post post) {
        this.value = value;
        this.user = user;
        this.post = post;
    }

    public Long getId() { return id; }
    public int getValue() { return value; }
    public void setValue(int value) { this.value = value; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
    public Post getPost() { return post; }
    public void setPost(Post post) { this.post = post; }
}
