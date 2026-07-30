package com.app.mapper;


import com.app.dto.CommunityDto;
import com.app.model.Community;
import org.mapstruct.Mapper;

// this DTO mirrors the Community object exactly
// this mapper is not really that necessary
// no fromDto() because the Service methods use the object s fields, not a dto for parameter
// the Service methods take those fields and check DB to return an object, no DTO needed in this process

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    CommunityDto toDto(Community community);
}
