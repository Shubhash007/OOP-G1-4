package com.example.timperio.crm.mapper;

import com.example.timperio.crm.dto.UserDto;
import com.example.timperio.crm.entity.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(user.getRowNo(), user.getUsername(), user.getPassword(), user.getRole());
    }

    public static User mapToUser(UserDto userDto) {
        return new User(userDto.getRowNo(), userDto.getUsername(), userDto.getPassword(), userDto.getRole());
    }
}
