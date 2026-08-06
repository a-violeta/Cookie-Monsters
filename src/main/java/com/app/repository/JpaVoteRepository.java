package com.app.repository;

import com.app.model.Vote;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

@Primary
public interface JpaVoteRepository extends JpaRepository<Vote, UUID>, VoteRepository {
}