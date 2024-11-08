package com.example.timperio.crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.dto.UserDto;
import com.example.timperio.crm.entity.User;
import com.example.timperio.crm.mapper.UserMapper;
import com.example.timperio.crm.repository.UserRepository;
import com.example.timperio.crm.service.UserService;
import java.util.*;

import com.example.timperio.crm.reusables.exceptions.*;

public interface UserService {
    UserDto login(String username, String password);

    // boolean updateOwnPassword(String username, String oldPassword, String
    // newPassword);
}