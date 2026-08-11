package com.app.repository;

import com.app.model.Post;
import com.app.model.PostVote;
import com.app.model.User;

import java.util.Optional;

public interface PostVoteRepository {
    PostVote save(PostVote vote);
    Optional<PostVote> findByPostAndAuthor(Post post, User currentUser);
}
