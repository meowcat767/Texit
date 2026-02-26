package site.meowcat.texit.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import site.meowcat.texit.backend.model.User;
import site.meowcat.texit.backend.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User createUser(String username, String password) {
        User user = new User(username, passwordEncoder.encode(password));
        if ("meowcat767".equals(username)) {
            user.setRole("ADMIN");
        }
        return userRepository.save(user);
    }

    public Optional<User> findByUsername(String username) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && "meowcat767".equals(username) && !"ADMIN".equals(userOpt.get().getRole())) {
            User user = userOpt.get();
            user.setRole("ADMIN");
            userRepository.save(user);
        }
        return userOpt;
    }

    public Optional<User> findById(long id) {
        return userRepository.findById(id);
    }

    public java.util.List<User> findAll() {
        return userRepository.findAll();
    }

    public User save(User user) {
        return userRepository.save(user);
    }
}
