package com.app.mapper;


import com.app.dto.CommunityDto;
import com.app.model.Community;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    CommunityDto toDto(Community community);
    Community fromDto(CommunityDto dto);
}
