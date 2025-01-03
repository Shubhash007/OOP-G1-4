package com.example.timperio.crm.timperio_g1_4.mapper;

import com.example.timperio.crm.timperio_g1_4.dto.UserDto;
import com.example.timperio.crm.timperio_g1_4.entity.User;

public class UserMapper {
    public static UserDto mapToUserDto(User user) {
        return new UserDto(user.getUserId(), user.getUsername(), user.getPassword(), user.getRole());
    }

    public static User mapToUser(UserDto userDto) {
        return new User(userDto.getRowNo(), userDto.getUsername(), userDto.getPassword(), userDto.getRole());
    }
}
