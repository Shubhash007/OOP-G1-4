package com.example.timperio.crm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.dto.UserDto;
import com.example.timperio.crm.entity.User;
import com.example.timperio.crm.mapper.UserMapper;
import com.example.timperio.crm.repository.UserRepository;
import com.example.timperio.crm.service.UserService;
import java.util.*;

import com.example.timperio.crm.reusables.exceptions.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDto login(String username, String password) {
        // TODO: how retieve user for loggin in?
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return null;
        }
        // should compare the password input after hashing w the value stored in db
        return UserMapper.mapToUserDto(user);
    }
}
