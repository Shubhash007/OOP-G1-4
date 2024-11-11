package com.example.timperio.crm.timperio_g1_4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.dto.UserDto;
import com.example.timperio.crm.timperio_g1_4.entity.AuthRequest;
import com.example.timperio.crm.timperio_g1_4.entity.User;
import com.example.timperio.crm.timperio_g1_4.service.JwtService;
import com.example.timperio.crm.timperio_g1_4.service.UserInfoService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoService userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // TODO: to adjust such that only admin can add new user
    @PostMapping("/addNewUser")
    public ResponseEntity<UserDto> addNewUser(@RequestBody UserDto userDto) {
        User savedUser = userService.addUser(userDto);
        return savedUser != null ? new ResponseEntity<UserDto>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/createUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User savedUser = userService.addUser(userDto);
        return savedUser != null ? new ResponseEntity<UserDto>(HttpStatus.CREATED)
                : new ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST);
    }

    // TODO: Create new custom exception for bad credentials
    @PostMapping("/generateToken")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(jwtService.generateToken(authRequest.getUsername()),
                    HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }
}
