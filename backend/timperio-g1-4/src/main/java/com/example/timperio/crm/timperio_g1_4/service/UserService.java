package com.example.timperio.crm.timperio_g1_4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.UserDto;
import com.example.timperio.crm.timperio_g1_4.entity.User;
import com.example.timperio.crm.timperio_g1_4.mapper.UserMapper;
import com.example.timperio.crm.timperio_g1_4.repository.UserRepository;
import com.example.timperio.crm.timperio_g1_4.service.UserService;
import java.util.*;

public interface UserService {
    UserDto createUser(UserDto userDto);

    UserDto login(String username, String password);

    //boolean updateOwnPassword(String username, String oldPassword, String newPassword);
}
