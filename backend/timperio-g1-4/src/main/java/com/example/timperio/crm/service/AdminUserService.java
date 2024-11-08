package com.example.timperio.crm.service;

import com.example.timperio.crm.dto.UserDto;
import com.example.timperio.crm.reusables.exceptions.UserAlreadyExistsException;
import com.example.timperio.crm.reusables.exceptions.UserNotFoundException;

public interface AdminUserService {
    UserDto createUser(UserDto userDto) throws UserAlreadyExistsException;

    UserDto updateUser(UserDto userDto) throws UserNotFoundException;

    UserDto deleteUser(int userId) throws UserNotFoundException;
}
