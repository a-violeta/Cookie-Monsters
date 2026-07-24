package com.app.repository;

import com.app.model.User;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);
    boolean existsByEmail(String email);

    User save(User user);
    void delete(User user);
    List<User> findAll();
    boolean existsByUsername(String username);
}