package com.example.timperio.crm.timperio_g1_4.dto;

import java.util.Optional;

import com.example.timperio.crm.timperio_g1_4.enums.Role;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String username;
    private Optional<String> newUsername;
    private Optional<String> newPassword;
    private Optional<Role> newRole;
}
