package org.pawel.zad9.service.impl;

import org.pawel.zad9.dto.UserRequest;
import org.pawel.zad9.model.Role;
import org.pawel.zad9.model.User;
import org.pawel.zad9.repository.RoleRepository;
import org.pawel.zad9.repository.UserRepository;
import org.pawel.zad9.service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(UserRequest req) {
        if(userRepository.findByLogin(req.getLogin()).isPresent()){
            throw new IllegalArgumentException("Error! Uzytkownik o podanym loginie juz istnieje");
        }
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("There is no role... ROLE_USER"));

        User u = User.builder()
                .id(UUID.randomUUID().toString())
                .login(req.getLogin())
                .password(passwordEncoder.encode(req.getPassword()))
                .isActive(true)
                .roles(Set.of(userRole))
                .build();

        userRepository.save(u);
    }

    @Override
    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Override
    public void deleteById(String id) {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            user.get().setActive(false);
            userRepository.save(user.get());
        }
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public boolean giveRole(String userId, String roleName) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findByName(roleName);
        if(user.isPresent() && role.isPresent()){
            if(user.get().isActive()){
                user.get().getRoles().add(role.get());
                userRepository.save(user.get());
                return true;
            }
        }
        return false;
    }

    @Override
    public void divestRole(String userId, String roleName) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findByName(roleName);
        if(user.isPresent() && role.isPresent()){
            if(user.get().isActive()){
                user.get().getRoles().remove(role.get());
                userRepository.save(user.get());
            }
        }
    }
}
