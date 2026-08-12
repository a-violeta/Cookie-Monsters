package com.app.mapper;

import com.app.dto.CommentDto;
import com.app.model.Comment;
import com.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

// Comment references User/Post objects so we want to use a Comment dto with ids for user and post
// username is added for easier display
// so the mapper reads off a Comment coming from DB and trusts that it's correct
// no fromDto() because the Service methods use the object s fields, not a dto for parameter
// the Service methods take those fields and check DB to return an object, no DTO needed in this process

@Mapper(componentModel = "spring")


public interface CommentMapper {
    @Mapping(target = "author", source = "author", qualifiedByName = "authorDisplayName")
    @Mapping(target = "postId", source = "post.id")
    @Mapping(target = "parentId", source = "parent.id")
    CommentDto toDto(Comment comment);

    // same convention as PostMapper.authorDisplayName - defensive null check plus
    // "[deleted user]" placeholder for soft-deleted authors on old comments
    @Named("authorDisplayName")
    default String authorDisplayName(User author) {
        if (author == null) {
            return null;
        }
        return author.isDeleted() ? "[deleted user]" : author.getUsername();
    }
}