package com.pawel.services;

import com.pawel.models.User;
import com.pawel.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {
    private final UserRepository userRepo;

    public AuthService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    public boolean register(String login, String rawPassword, String role) {
        if (userRepo.findByLogin(login).isPresent()) {
            System.out.println("Uzytkownik o tym loginie juz istnieje.");
            return false;
        }

        String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

        User user = User.builder()
                .login(login)
                .password(hashed)
                .role(role)
                .build();

        userRepo.save(user);
        System.out.println("Zarejestrowano pomyslnie.");
        return true;
    }

    public Optional<User> login(String login, String rawPassword) {
        return userRepo.findByLogin(login)
                .filter(user -> BCrypt.checkpw(rawPassword, user.getPassword()));
    }
}
