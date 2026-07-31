package com.app.mapper;

import com.app.dto.UserDto;
import com.app.model.User;
import org.mapstruct.Mapper;

// this DTO mirrors the User object exactly
// no fromDto() because the Service methods use the object s fields, not a dto for parameter
// the Service methods take those fields and check DB to return an object, no DTO needed in this process

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}