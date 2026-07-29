package com.app.mapper;

import com.app.dto.UserDto;
import com.app.model.User;
import org.mapstruct.Mapper;

/**
 * User -> UserDto mapping, one direction only.
 *
 * WHY toDto() IS HERE: same reasoning as CommunityMapper -- User's relevant
 * fields (username, email, description, createdAt) mirror UserDto's fields
 * directly, no id flattening needed, so MapStruct just saves the manual
 * getter/setter copying.
 *
 * WHY password IS SAFE HERE: UserDto has no @Mapping(ignore) or exclusion
 * rule for password -- it's simply not a field MapStruct is told to touch
 * in this one-directional mapping, since toDto() only ever reads FROM User
 * INTO UserDto for building a response. Nothing forces password through this
 * path. The risk would only appear if a fromDto() existed and got used to
 * update a User from client input -- not present here on purpose.
 *
 * WHY THERE IS NO fromDto(): creating a user needs validation, uniqueness
 * checks (username/email not already taken), and password handling that
 * live in UserService.createUser(...). A mapper-built User would bypass all
 * of that. So UserController never asks this mapper to build a User -- it
 * unpacks UserDto's primitive fields and calls userService.createUser(...)
 * directly, same pattern as Post/Comment.
 */

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}