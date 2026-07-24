package com.app.repository;

import com.app.model.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

@Primary
public interface JpaUserRepository extends JpaRepository<User, Long>, UserRepository {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);

    boolean existsByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
}