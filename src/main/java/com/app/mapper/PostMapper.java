package com.app.mapper;

import com.app.dto.PostDto;
import com.app.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// no fromDto() because the Service methods use the object's fields, not a dto for parameter
// the Service methods take those fields and check DB to return an object, no DTO needed in this process

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "subreddit", source = "subreddit.name")
    @Mapping(target = "author", source = "author.username")
    @Mapping(target = "imageUrl", source = "media.path")
    PostDto toDto(Post post);
}