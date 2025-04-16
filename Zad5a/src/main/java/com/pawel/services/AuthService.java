package com.pawel.services;

import com.pawel.models.User;

import java.util.Optional;

public interface AuthService {
    boolean register(String login, String rawPassword, String role);
    Optional<User> login(String login, String rawPassword);
}
