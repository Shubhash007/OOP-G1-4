package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.UserDto;
import com.example.timperio.crm.timperio_g1_4.dto.UserUpdateRequest;
import com.example.timperio.crm.timperio_g1_4.entity.User;
import com.example.timperio.crm.timperio_g1_4.enums.Role;
import com.example.timperio.crm.timperio_g1_4.mapper.UserMapper;
import com.example.timperio.crm.timperio_g1_4.repository.UserRepository;
import com.example.timperio.crm.timperio_g1_4.service.UserInfoDetails;

@Service
public class UserServiceImpl implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    // cannot change as the method name is from UserDetailsService (import from
    // spring)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userDetail = userRepository.findByUsername(username);
        // Converting UserInfo to UserDetails
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));
    }

    public List<User> getAllUsers() throws Exception {
        List<User> userList;
        try {
            userList = userRepository.findAll();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        for (User user : userList) {
            user.setPassword(null);
        }
        return userList;
    }

    public User createUser(UserDto userDto) throws BadCredentialsException, IllegalArgumentException {
        // check if all fields are present
        if (userDto.getUsername() == null || userDto.getPassword() == null || userDto.getRole() == null) {
            throw new IllegalArgumentException("Check if username, password and role are present");
        }

        // check if the username is already taken
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new BadCredentialsException("Username already taken");
        }

        // Encode password before saving the user
        userDto.setPassword(encoder.encode(userDto.getPassword()));
        userRepository.save(UserMapper.mapToUser(userDto));

        // return the user but mask the password
        userDto.setPassword(null);
        return UserMapper.mapToUser(userDto);
    }

    public boolean updateUserPassword(String username, String newPassword)
            throws UsernameNotFoundException, Exception {
        Optional<User> dbUser = userRepository.findByUsername(username);
        if (dbUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        try {
            User user = dbUser.get();
            user.setPassword(encoder.encode(newPassword));
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean updateUser(UserUpdateRequest userUpdateRequest)
            throws UsernameNotFoundException, BadCredentialsException, Exception {
        Optional<User> dbUser = userRepository.findByUsername(userUpdateRequest.getUsername());
        if (dbUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        try {
            User user = dbUser.get();
            if (user.getRole() == Role.ROLE_ADMIN) {
                throw new BadCredentialsException("Cannot update admin user");
            }
            userUpdateRequest.getNewUsername().ifPresent(user::setUsername);
            userUpdateRequest.getNewPassword().ifPresent(password -> {
                String hashedPassword = encoder.encode(password); // Hash the password
                user.setPassword(hashedPassword);
            });
            userUpdateRequest.getNewRole().ifPresent(user::setRole);
            userRepository.save(user);
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    public boolean deleteUser(String username) throws UsernameNotFoundException, Exception {
        Optional<User> dbUser = userRepository.findByUsername(username);
        if (dbUser.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        try {
            userRepository.delete(dbUser.get());
            return true;
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}