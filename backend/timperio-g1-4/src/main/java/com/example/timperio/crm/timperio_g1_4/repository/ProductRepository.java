package com.example.timperio.crm.timperio_g1_4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.timperio.crm.timperio_g1_4.entity.Product;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);

    Product findByProductNameAndVariant(String productName, Integer variant);

    List<Product> findByProductNameContaining(String productName);
}