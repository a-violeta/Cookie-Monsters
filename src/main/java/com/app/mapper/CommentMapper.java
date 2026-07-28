package com.app.mapper;

import com.app.dto.CommentDto;
import com.app.model.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "postId", source = "post.id")
    CommentDto toDto(Comment comment);
    // no fromDto because service classes take ids for parameters, not full objects
    // so this would be useless or even bypass the validations
}