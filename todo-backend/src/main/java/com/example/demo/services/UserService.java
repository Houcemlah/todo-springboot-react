package com.example.demo.services;

import com.example.demo.entities.User;
import com.example.demo.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService extends GenericServices<User, Long> {

    private final UserRepository repo;

    protected UserService(UserRepository repo) {
        super();
        this.repo = repo;
    }

    @Override
    protected UserRepository getRepository() {
        return this.repo;
    }

    public Optional<User> findByEmail(String email){
        return repo.findByEmail(email);
    }
    public Optional<User> update(Long id, User updated) {
        return repo.findById(id).map(u -> {
            u.setName(updated.getName());
            u.setEmail(updated.getEmail());
            return repo.save(u);
        });
    }
}
