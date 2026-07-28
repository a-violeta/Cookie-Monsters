package com.app.mapper;

import com.app.dto.PostDto;
import com.app.model.Community;
import com.app.model.Post;
import com.app.model.User;
import com.app.repository.CommunityRepository;
import com.app.repository.UserRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class PostMapper {

    @Autowired
    protected CommunityRepository communityRepository;
    @Autowired
    protected UserRepository userRepository;

    @Mapping(target = "communityId", source = "community.id")
    @Mapping(target = "communityName", source = "community.communityName")
    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    public abstract PostDto toDto(Post post);
    // no fromDto because service classes take ids for parameters, not full objects
    // so this would be useless or even bypass the validations
}