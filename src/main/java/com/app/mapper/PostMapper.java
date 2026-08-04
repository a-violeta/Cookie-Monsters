package com.app.mapper;

import com.app.dto.PostDto;
import com.app.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

// Post references User/Community objects so we want to use a Post dto with ids for user and community
// author and subreddit name are added for easier display
// no need to check DB just for the author or subreddit name of a post's user/community
// so the mapper reads off a Comment coming from DB and trusts that it's correct
// no fromDto() because the Service methods use the object's fields, not a dto for parameter
// the Service methods take those fields and check DB to return an object, no DTO needed in this process

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "communityId", source = "subreddit.id")
    @Mapping(target = "subreddit", source = "subreddit.name")
    @Mapping(target = "userId", source = "author.id")
    @Mapping(target = "author", source = "author.username")
    PostDto toDto(Post post);
}