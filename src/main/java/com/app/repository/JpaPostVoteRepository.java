package com.app.repository;

import com.app.model.PostVote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaPostVoteRepository extends JpaRepository<PostVote, UUID>, PostVoteRepository {
}
