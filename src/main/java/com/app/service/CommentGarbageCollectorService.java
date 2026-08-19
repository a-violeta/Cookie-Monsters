package com.app.service;

import com.app.model.Comment;
import com.app.model.Post;
import com.app.repository.CommentRepository;
import com.app.repository.CommentVoteRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentGarbageCollectorService {

    private final CommentRepository commentRepository;
    private final CommentVoteRepository commentVoteRepository;
    private final AsyncLoggerService logger;
    private final EntityManager entityManager;

    @Transactional
    public void cleanupGhostParent(Comment parent) {

        if (parent == null || !parent.isDeleted()
                || parent.getReplies() == null || !parent.getReplies().isEmpty()) {
            return;
        }

        logger.logInfo("Comment Garbage Collector Started");

        Comment grandParent = parent.getParent();
        Post post = parent.getPost();

        if (grandParent != null && grandParent.getReplies() != null) {
            grandParent.getReplies().remove(parent);
        }
        if (post != null && post.getCommentList() != null) {
            post.getCommentList().remove(parent);
        }

        parent.setParent(null);
        parent.setPost(null);
        parent.setAuthor(null);

        entityManager.flush();

        commentVoteRepository.deleteAllByComment(parent);
        commentRepository.delete(parent);
        entityManager.flush();

        logger.logInfo("GC hard deleted comment id = " + parent.getId());

        cleanupGhostParent(grandParent);
    }
}