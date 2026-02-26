package site.meowcat.texit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.meowcat.texit.backend.model.Post;
import site.meowcat.texit.backend.model.User;
import site.meowcat.texit.backend.model.Vote;
import site.meowcat.texit.backend.repository.PostRepository;
import site.meowcat.texit.backend.repository.VoteRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

@Service
public class VoteService {
    private static final Logger logger = LoggerFactory.getLogger(VoteService.class);

    @Autowired
    private VoteRepository voteRepository;

    @Autowired
    private PostRepository postRepository;

    @Transactional
    public void vote(Post post, User user, int value) {
        if (value != 1 && value != -1 && value != 0) {
            throw new IllegalArgumentException("Vote value must be 1, -1 or 0");
        }

        Optional<Vote> existingVote = voteRepository.findByPostAndUser(post, user);
        logger.info("User {} is voting {} on post {}", user.getUsername(), value, post.getId());

        if (existingVote.isPresent()) {
            Vote vote = existingVote.get();
            int oldValue = vote.getValue();
            if (value == 0) {
                voteRepository.delete(vote);
                post.setVotes(post.getVotes() - oldValue);
            } else {
                vote.setValue(value);
                voteRepository.save(vote);
                post.setVotes(post.getVotes() - oldValue + value);
            }
        } else if (value != 0) {
            Vote vote = new Vote(value, user, post);
            voteRepository.save(vote);
            post.setVotes(post.getVotes() + value);
        }
        postRepository.save(post);
    }
}
