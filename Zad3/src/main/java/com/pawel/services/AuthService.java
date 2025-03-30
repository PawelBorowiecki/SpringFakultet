package com.pawel.services;

import com.pawel.models.User;
import com.pawel.repositories.UserRepository;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalTime;
import java.util.Optional;
import java.util.UUID;

public class AuthService {
    public Optional<User> login(String inputLogin, String inputPassword, UserRepository usersRepo){
        Optional<User> user = usersRepo.findByLogin(inputLogin)
                .filter(u -> BCrypt.checkpw(inputPassword, u.getPassword()));
        return user;
    }

    public boolean register(String inputLogin, String inputPassword, String inputRole, UserRepository usersRepo, User user){
        Optional<User> userFromRepo = usersRepo.findByLogin(inputLogin);
        if(userFromRepo.isEmpty()){
            String hashedPassword = BCrypt.hashpw(inputPassword, BCrypt.gensalt());
            user.setId(UUID.randomUUID().toString() + LocalTime.now().toString());
            user.setLogin(inputLogin);
            user.setPassword(hashedPassword);
            user.setRole(inputRole);
            usersRepo.addUser(user);
            return true;
        }
        return false;
    }
}
