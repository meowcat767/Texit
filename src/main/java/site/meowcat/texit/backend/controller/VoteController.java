package site.meowcat.texit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.meowcat.texit.backend.model.Post;
import site.meowcat.texit.backend.model.User;
import site.meowcat.texit.backend.service.PostService;
import site.meowcat.texit.backend.service.UserService;
import site.meowcat.texit.backend.service.VoteService;

@RestController
@RequestMapping("/api/votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @PostMapping("/post/{postId}")
    public void vote(@PathVariable Long postId, @RequestBody VoteRequest voteRequest) {
        Post post = postService.getPostById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userService.findByUsername(voteRequest.getUsername())
                .orElseGet(() -> userService.createUser(voteRequest.getUsername(), "password"));
        voteService.vote(post, user, voteRequest.getValue());
    }

    public static class VoteRequest {
        private String username;
        private int value; // 1, -1, or 0 to remove

        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public int getValue() { return value; }
        public void setValue(int value) { this.value = value; }
    }
}
