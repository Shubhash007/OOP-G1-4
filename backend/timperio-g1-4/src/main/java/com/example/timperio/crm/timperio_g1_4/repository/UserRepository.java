package com.example.timperio.crm.timperio_g1_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.timperio.crm.timperio_g1_4.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {

}