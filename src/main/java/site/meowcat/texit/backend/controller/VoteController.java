package site.meowcat.texit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
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
    public void vote(@PathVariable Long postId, @RequestBody VoteRequest voteRequest, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.getPostById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        User user = userService.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        voteService.vote(post, user, voteRequest.getValue());
    }

    public static class VoteRequest {
        private int value; // 1, -1, or 0 to remove

        public int getValue() { return value; }
        public void setValue(int value) { this.value = value; }
    }
}
