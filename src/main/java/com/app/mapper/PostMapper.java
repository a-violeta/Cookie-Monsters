package com.app.mapper;

import com.app.dto.PostDto;
import com.app.model.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mapping(target = "communityId", source = "community.id")
    @Mapping(target = "communityName", source = "community.communityName")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    PostDto toDto(Post post);
}