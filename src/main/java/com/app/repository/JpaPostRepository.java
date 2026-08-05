package com.app.repository;

import com.app.model.Post;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Primary
// Fixed the ID type from Long to UUID to match the exact Post entity primary key
public interface JpaPostRepository extends JpaRepository<Post, UUID>, PostRepository {
}