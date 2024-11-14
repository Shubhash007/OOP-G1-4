package com.example.timperio.crm.timperio_g1_4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.dto.PasswordUpdateRequest;
import com.example.timperio.crm.timperio_g1_4.dto.UserDto;
import com.example.timperio.crm.timperio_g1_4.dto.UserUpdateRequest;
import com.example.timperio.crm.timperio_g1_4.entity.AuthRequest;
import com.example.timperio.crm.timperio_g1_4.entity.User;
import com.example.timperio.crm.timperio_g1_4.service.JwtService;
import com.example.timperio.crm.timperio_g1_4.service.UserInfoDetails;
import com.example.timperio.crm.timperio_g1_4.service.impl.UserServiceImpl;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    // TODO: deprecate this endpoint before submission - maybe create a default
    // admin user when the database is created
    @PostMapping("/addNewUser")
    public ResponseEntity<UserDto> addNewUser(@RequestBody UserDto userDto) {
        User savedUser = userService.addUser(userDto);
        return savedUser != null ? new ResponseEntity<UserDto>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    // TODO: Create new custom exception for bad credentials (optional)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return new ResponseEntity<>(jwtService.generateToken(authRequest.getUsername()),
                    HttpStatus.OK);
        } else {
            throw new UsernameNotFoundException("Invalid username or password");
        }
    }

    // this endpoint gets the info of the current logged on user
    @GetMapping("/users/get-user")
    @PreAuthorize("isAuthenticated()") // we check if the user is already logged in
    public ResponseEntity<?> getCurrentUser() throws UsernameNotFoundException, Exception {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            // Ensure the authentication is not null and contains the expected principal
            if (authentication != null && authentication.getPrincipal() instanceof UserInfoDetails) {
                UserInfoDetails userInfoDetails = (UserInfoDetails) authentication.getPrincipal();
                User user = userInfoDetails.getUser(); // Assuming UserInfoDetails has a getUser() method
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User not found or unauthorized", HttpStatus.FORBIDDEN);
            }
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<String>("Username not found", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>("An error has occured", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/users/update-password")
    @PreAuthorize("isAuthenticated()") // we check if the user is already logged in
    public ResponseEntity<String> updatePassword(@RequestBody PasswordUpdateRequest newPassword)
            throws BadCredentialsException {
        try {
            if (newPassword == null || newPassword.getNewPassword().isEmpty()) {
                return new ResponseEntity<String>("Password cannot be empty", HttpStatus.BAD_REQUEST);
            }
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            boolean updated = userService.updateUserPassword(username, newPassword.getNewPassword());
            return updated ? new ResponseEntity<String>("Password updated successfully", HttpStatus.OK)
                    : new ResponseEntity<String>("An unknown error occured", HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<String>("Incorrect password provided", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/admin/getAllUsers")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }

    // To be modified to the conventions
    @PostMapping("/admin/createUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<UserDto> createUser(@RequestBody UserDto userDto) {
        User savedUser = userService.addUser(userDto);
        return savedUser != null ? new ResponseEntity<UserDto>(HttpStatus.CREATED)
                : new ResponseEntity<UserDto>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/admin/updateUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateUser(@RequestBody UserUpdateRequest UserUpdateRequest) {
        try {
            Boolean updated = userService.updateUser(UserUpdateRequest);
            return updated ? new ResponseEntity<String>("User updated successfully", HttpStatus.OK)
                    : new ResponseEntity<String>("User update failed", HttpStatus.BAD_REQUEST);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<String>("User not found", HttpStatus.BAD_REQUEST);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<String>("Cannot update admin user", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/admin/deleteUser")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteUser(@RequestParam String username) {
        try {
            Boolean deleted = userService.deleteUser(username);
            return deleted ? new ResponseEntity<String>("User deleted successfully", HttpStatus.OK)
                    : new ResponseEntity<String>("User deletion failed", HttpStatus.BAD_REQUEST);
        } catch (UsernameNotFoundException e) {
            return new ResponseEntity<String>("User not found", HttpStatus.BAD_REQUEST);
        }

    }
}
