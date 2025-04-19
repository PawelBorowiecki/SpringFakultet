package com.pawel.services.impl.hibernate;

import com.pawel.db.HibernateConfig;
import com.pawel.models.User;
import com.pawel.repositories.impl.hibernate.UserHibernateRepository;
import com.pawel.services.AuthService;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.UUID;

public class AuthHibernateService implements AuthService {
    private final UserHibernateRepository userRepo;

    public AuthHibernateService(UserHibernateRepository userHibernateRepository) {
        this.userRepo = userHibernateRepository;
    }

    @Override
    public boolean register(String login, String rawPassword, String role) {
        try(Session session = HibernateConfig.getSessionFactory().openSession()){
            Transaction tx = session.beginTransaction();
            userRepo.setSession(session);
            if (userRepo.findByLogin(login).isPresent()) {
                return false;
            }

            String hashed = BCrypt.hashpw(rawPassword, BCrypt.gensalt());

            User user = User.builder()
                    .id(UUID.randomUUID().toString())
                    .login(login)
                    .password(hashed)
                    .role(role)
                    .build();

            userRepo.save(user);
            tx.commit();
            return true;
        }
    }

    @Override
    public Optional<User> login(String login, String rawPassword) {
        try (Session session = HibernateConfig.getSessionFactory().openSession()){
            userRepo.setSession(session);
            return userRepo.findByLogin(login)
                    .filter(user -> BCrypt.checkpw(rawPassword, user.getPassword()));
        }
    }
}
