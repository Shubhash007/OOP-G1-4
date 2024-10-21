package com.example.timperio.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.timperio.crm.entity.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

}
