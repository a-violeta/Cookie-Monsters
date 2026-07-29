package com.app.mapper;

import com.app.dto.CommentDto;
import com.app.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Comment -> CommentDto mapping (read direction only).
 *
 * Same reasoning as PostMapper: Comment references User/Post as real objects,
 * CommentDto flattens to userId/postId (+ username for display). This mapper
 * only reads already-loaded associations off a Comment that CommentService
 * fetched -- no independent lookup happening here.
 *
 * No fromDto() for the same reason as Post: CommentService.addComment(text,
 * userId, postId) is where the real lookups AND the membership check
 * ("is this user part of the community this post belongs to?") happen. A
 * mapper-built Comment would bypass that check entirely, so the controller
 * calls the service with primitives instead of asking this mapper to build
 * an entity.
 */

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "postId", source = "post.id")
    CommentDto toDto(Comment comment);
    // no fromDto because service classes take ids for parameters, not full objects
    // so this would be useless or even bypass the validations
}