package site.meowcat.texit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import site.meowcat.texit.backend.model.Post;
import site.meowcat.texit.backend.model.User;
import site.meowcat.texit.backend.repository.PostRepository;
import site.meowcat.texit.backend.repository.CommentRepository;
import site.meowcat.texit.backend.repository.VoteRepository;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private VoteRepository voteRepository;

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    public Post createPost(String title, String body, User author) {
        Post post = new Post(title, body, author);
        return postRepository.save(post);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post save(Post post) {
        return postRepository.save(post);
    }

    @org.springframework.transaction.annotation.Transactional
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("Post not found"));
        commentRepository.deleteByPost(post);
        voteRepository.deleteByPost(post);
        postRepository.delete(post);
    }
}
