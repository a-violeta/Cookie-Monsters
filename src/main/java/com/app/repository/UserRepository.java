package com.app.repository;

import com.app.model.User;

import java.util.List;
import java.util.Optional;

// interface for user repo
public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    User save(User user);
    void delete(User user);
    List<User> findAll();
    boolean existsByUsername(String username);
}