package com.pawel.zad8.service;

import com.pawel.zad8.dto.UserRequest;
import com.pawel.zad8.model.Role;
import com.pawel.zad8.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService {
    void register(UserRequest req);
    Optional<User> findById(String id);
    Optional<User> findByLogin(String login);
    void deleteById(String id);
    List<User> getUsers();
    boolean giveRole(String userId, String roleName);
    void divestRole(String userId, String roleName);
}
