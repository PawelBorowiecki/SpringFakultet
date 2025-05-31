package com.pawel.zad8.controller;

import com.pawel.zad8.model.User;
import com.pawel.zad8.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/stats")
    public ResponseEntity<List<User>> getUsersStatistics(@AuthenticationPrincipal UserDetails userDetails){
        String login = userDetails.getUsername();
        Optional<User> user = userService.findByLogin(login);
        List<User> users = null;
        if(user.isPresent()){
            if(user.get().getRoles().stream().anyMatch(r -> r.getName().equals("ROLE_ADMIN"))){
                users = userService.getUsers();
            }else{
                users = List.of(user.get());
            }
        }
        if(users != null){
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/give{roleName}={userId}")
    public ResponseEntity<?> giveRoleForUser(@PathVariable String roleName, @PathVariable String userId){
        Optional<User> user = userService.findById(userId);
        if(user.isPresent()){
            if(user.get().isActive()){
                userService.giveRole(userId, roleName);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/divest{roleName}={userId}")
    public ResponseEntity<?> divestRole(@PathVariable String roleName, @PathVariable String userId){
        Optional<User> user = userService.findById(userId);
        if(user.isPresent()){
            if(user.get().isActive()){
                userService.divestRole(userId, roleName);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.badRequest().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String id){
        Optional<User> user = userService.findById(id);
        if(user.isPresent()){
            if(user.get().isActive()){
                userService.deleteById(id);
                return ResponseEntity.ok().build();
            }
        }
        return ResponseEntity.notFound().build();
    }
}
