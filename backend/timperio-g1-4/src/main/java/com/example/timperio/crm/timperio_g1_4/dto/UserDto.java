package com.example.timperio.crm.timperio_g1_4.dto;

import com.example.timperio.crm.timperio_g1_4.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long rowNo;
    private String username;
    private String password;
    private Role role;
}
