package com.app.mapper;

import com.app.dto.PostDto;
import com.app.model.Post;
import com.app.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

// no fromDto() because the Service methods use the object's fields, not a dto for parameter
// the Service methods take those fields and check DB to return an object, no DTO needed in this process

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(target = "subreddit", source = "subreddit.name")
    @Mapping(target = "author", source = "author", qualifiedByName = "authorDisplayName")
    @Mapping(target = "imageUrl", source = "media.path")
    @Mapping(target = "filter", source = "media.filter")
    PostDto toDto(Post post);

    // defensive null check even though author is a required FK (never actually null);
    // the real case this handles is a soft-deleted author - row still exists, but we
    // don't want to keep showing their real username on old posts/comments
    @Named("authorDisplayName")
    default String authorDisplayName(User author) {
        if (author == null) {
            return null;
        }
        return author.isDeleted() ? "[deleted user]" : author.getUsername();
    }
}