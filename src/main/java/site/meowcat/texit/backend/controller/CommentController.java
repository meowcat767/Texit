package site.meowcat.texit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.meowcat.texit.backend.model.Comment;
import site.meowcat.texit.backend.model.Post;
import site.meowcat.texit.backend.model.User;
import site.meowcat.texit.backend.service.CommentService;
import site.meowcat.texit.backend.service.PostService;
import site.meowcat.texit.backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping("/post/{postId}")
    public List<Comment> getComments(@PathVariable Long postId) {
        Post post = postService.getPostById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        return commentService.getCommentsForPost(post);
    }

    @PostMapping("/post/{postId}")
    public Comment addComment(@PathVariable Long postId, @RequestBody CommentRequest commentRequest) {
        Post post = postService.getPostById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User author = userService.findByUsername(commentRequest.getUsername())
                .orElseGet(() -> userService.createUser(commentRequest.getUsername(), "password"));
        return commentService.addComment(commentRequest.getBody(), author, post);
    }

    public static class CommentRequest {
        private String body;
        private String username;

        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
}
