package com.app.repository;

import com.app.model.Post;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

@Primary
public interface JpaPostRepository extends JpaRepository<Post, Long>, PostRepository {
}
