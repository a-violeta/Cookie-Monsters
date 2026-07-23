package com.app.repository;

import com.app.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaPostRepository extends JpaRepository<Post, Long>, PostRepository {
}
