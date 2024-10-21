package com.example.timperio.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.timperio.crm.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
