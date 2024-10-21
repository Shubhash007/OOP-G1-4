package com.example.timperio.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.timperio.crm.entity.User;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);
}
