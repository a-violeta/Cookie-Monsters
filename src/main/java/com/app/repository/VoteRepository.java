package com.app.repository;

import com.app.model.Post;
import com.app.model.User;
import com.app.model.Vote;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository {
    Vote save(Vote vote);
    Optional<Vote> findByPostAndAuthor(Post post, User currentUser);
}