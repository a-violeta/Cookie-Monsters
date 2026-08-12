package com.app.mapper;


import com.app.dto.CommunityDto;
import com.app.model.Community;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/*this DTO mirrors the Community object exactly
 and it maps what memberCount and postCount are, sizes of lists
 no fromDto() because the Service methods use the object s fields, not a dto for parameter
 the Service methods take those fields and check DB to return an object, no DTO needed in this process

 */

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    @Mapping(target = "memberCount", expression = "java(community.getCommunityUsers() == null ? 0 : community.getCommunityUsers().size())")
    @Mapping(target = "postCount", expression = "java(community.getCommunityPosts() == null ? 0 : community.getCommunityPosts().size())")
    CommunityDto toDto(Community community);
}