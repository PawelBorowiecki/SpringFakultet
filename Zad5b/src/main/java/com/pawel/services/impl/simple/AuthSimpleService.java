package com.pawel.services.impl.simple;

import com.pawel.models.User;
import com.pawel.repositories.UserRepository;
import com.pawel.services.AuthService;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthSimpleService implements AuthService {
    private final UserRepository userRepo;

    public AuthSimpleService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }
    @Override
    public boolean register(String login, String rawPassword, String role) {
        if (userRepo.findByLogin(login).isPresent()) {
            return false;
        }

        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User user = User.builder()
                .login(login)
                .password(hashed)
                .role(role)
                .build();

        userRepo.save(user);
        return true;
    }

    @Override
    public Optional<User> login(String login, String rawPassword) {
        return userRepo.findByLogin(login)
                .filter(user -> BCrypt.checkpw(rawPassword, user.getPassword()));
    }
}
