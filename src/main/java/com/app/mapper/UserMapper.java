package com.app.mapper;

import com.app.dto.UserDto;
import com.app.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}