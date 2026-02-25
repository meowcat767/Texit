package site.meowcat.texit.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Vote {
    @Id @GeneratedValue
    private Long id;
    private int value; // 1 = upvote, -1 = downvote
    @ManyToOne
    private User user;
    @ManyToOne
    private Post post;

}
