package com.example.timperio.crm.timperio_g1_4.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.timperio.crm.timperio_g1_4.dto.UserDto;
import com.example.timperio.crm.timperio_g1_4.dto.UserUpdateRequest;
import com.example.timperio.crm.timperio_g1_4.entity.User;

public interface UserService {
    UserDetails getUserByUsername(String username) throws UsernameNotFoundException;

    User addUser(UserDto userDto);

    boolean updateUserPassword(String username, String newPassword);

    boolean updateUser(UserUpdateRequest userUpdateRequest);
}