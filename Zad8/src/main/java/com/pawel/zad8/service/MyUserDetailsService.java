package com.pawel.zad8.service;

import com.pawel.zad8.model.User;
import com.pawel.zad8.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new UsernameNotFoundException("User" + username + " doesn't exist."));
        var authorities = user.getRoles().stream().map(role -> new SimpleGrantedAuthority(role.getName())).toList();
        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password("{bcrypt}" + user.getPassword())
                .authorities(authorities)
                .disabled(!user.isActive())
                .build();
    }
}
