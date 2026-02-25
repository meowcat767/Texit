package site.meowcat.texit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import site.meowcat.texit.backend.model.Post;
import site.meowcat.texit.backend.model.User;
import site.meowcat.texit.backend.service.PostService;
import site.meowcat.texit.backend.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UserService userService;

    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @PostMapping
    public Post createPost(@RequestBody PostRequest postRequest) {
        // Simplified: using a hardcoded user or from a fake header for now as we don't have full auth
        User author = userService.findByUsername(postRequest.getUsername())
                .orElseGet(() -> userService.createUser(postRequest.getUsername(), "password"));
        return postService.createPost(postRequest.getTitle(), postRequest.getBody(), author);
    }

    @GetMapping("/{id}")
    public Post getPost(@PathVariable Long id) {
        return postService.getPostById(id).orElseThrow(() -> new RuntimeException("Post not found"));
    }

    public static class PostRequest {
        private String title;
        private String body;
        private String username;

        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
    }
}
