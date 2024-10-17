package com.example.timperio.crm.timperio_g1_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.timperio.crm.timperio_g1_4.entity.User;
import java.util.List;


public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
