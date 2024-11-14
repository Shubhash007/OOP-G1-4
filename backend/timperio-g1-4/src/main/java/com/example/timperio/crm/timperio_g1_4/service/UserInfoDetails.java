package com.example.timperio.crm.timperio_g1_4.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties.Simple;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.timperio.crm.timperio_g1_4.entity.User;
import com.example.timperio.crm.timperio_g1_4.enums.Role;

public class UserInfoDetails implements UserDetails {
    private Long id;
    private String username;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoDetails(User userInfo) {
        this.id = userInfo.getUserId();
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority(userInfo.getRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public User getUser() {
        return new User(id, username, Role.valueOf(authorities.get(0).getAuthority()));
    }
}
