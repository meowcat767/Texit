package site.meowcat.texit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import site.meowcat.texit.backend.model.User;
import site.meowcat.texit.backend.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.createUser(user.getUsername(), user.getPassword());
    }

    @GetMapping("/me")
    public User getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return null;
        return userService.findByUsername(userDetails.getUsername()).orElse(null);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        return userService.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
