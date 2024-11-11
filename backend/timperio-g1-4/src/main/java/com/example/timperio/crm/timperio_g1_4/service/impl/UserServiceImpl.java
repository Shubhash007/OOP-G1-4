package com.example.timperio.crm.timperio_g1_4.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.UserDto;
import com.example.timperio.crm.timperio_g1_4.entity.User;
import com.example.timperio.crm.timperio_g1_4.mapper.UserMapper;
import com.example.timperio.crm.timperio_g1_4.repository.UserRepository;
import com.example.timperio.crm.timperio_g1_4.service.UserService;
import java.util.*;

// @Service
// public class UserServiceImpl implements UserService {
//     @Autowired
//     private UserRepository userRepository;

//     @Override
//     public UserDto createUser(UserDto userDto) {
//         User user = UserMapper.mapToUser(userDto);
//         User savedUser = userRepository.save(user);
//         return UserMapper.mapToUserDto(savedUser);
//     }

//     @Override
//     public UserDto login(String username, String password) {
//         // TODO: how retieve user for loggin in?
//         User user = userRepository.findByUsername(username);
//         if (user == null) {
//             return null;
//         }
//         // should compare the password input after hashing w the value stored in db
//         return UserMapper.mapToUserDto(user);
//     }
// }
