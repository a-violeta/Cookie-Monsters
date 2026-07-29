package com.app.mapper;


import com.app.dto.CommunityDto;
import com.app.model.Community;
import org.mapstruct.Mapper;

/**
 * Community <-> CommunityDto mapping.
 *
 * Why a mapper exists here at all: CommunityDto mirrors Community field-for-field
 * (communityName, description, createdAt) -- no id lookups, no flattening needed.
 * MapStruct just saves us writing the getter/setter copying by hand; there's no
 * real complexity being managed here.
 *
 * toDto():   used by CommunityController to build every response.
 * fromDto(): safe to keep but useless because
 *            CommunityService.createCommunity(name, description) doesn't need
 *            an id lookup or any validation this mapper would make,
 *            CommunityService already checks validity before creating the Community.
 *            For the User, a mapper-built entity would
 *            skip the password, username/email checks that live in UserService.
 */

@Mapper(componentModel = "spring")
public interface CommunityMapper {
    CommunityDto toDto(Community community);
    //Community fromDto(CommunityDto dto);
    // not needed
}
