package com.example.timperio.crm.timperio_g1_4.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.timperio.crm.timperio_g1_4.entity.User;
import com.example.timperio.crm.timperio_g1_4.enums.Role;
import com.example.timperio.crm.timperio_g1_4.repository.UserRepository;

@Component
public class AdminUserInitializer {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @EventListener
    public void onApplicationEvent(ContextRefreshedEvent event) throws Exception {
        try {
            if (userRepository.count() == 0) {
                User adminUser = new User();
                adminUser.setUsername("admin");
                adminUser.setPassword(passwordEncoder.encode("admin"));
                adminUser.setRole(Role.ROLE_ADMIN);
                userRepository.save(adminUser);
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }
}