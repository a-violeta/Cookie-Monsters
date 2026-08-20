package com.app.service;

import com.app.model.Comment;
import com.app.model.Post;
import com.app.model.User;
import com.app.repository.CommentRepository;
import com.app.repository.CommentVoteRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentGarbageCollectorService {

    private final AsyncLoggerService logger;

    @Transactional
    public void cleanupGhostParent(Comment parent) {

        if (parent != null && parent.isDeleted() && (parent.getReplies() == null || parent.getReplies().isEmpty())) {

            Comment grandParent = parent.getParent();
            Post post = parent.getPost();
            User author = parent.getAuthor();

            if (post != null && post.getCommentList() != null) {
                post.getCommentList().remove(parent);
                post.setCommentCount(post.getCommentCount() - 1);
            }

            if (grandParent != null && grandParent.getReplies() != null) {
                grandParent.getReplies().remove(parent);
            }

            if (author != null && author.getComments() != null) {
                author.getComments().remove(parent);
            }

            logger.logInfo("[GARBAGE COLLECTOR] Fantôme supprimé via Orphan Removal (ID: " + parent.getId() + ")");

            cleanupGhostParent(grandParent);
        }
    }
}