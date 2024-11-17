package com.example.timperio.crm.timperio_g1_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.timperio.crm.timperio_g1_4.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByProductId(Long productId);

    Product findByProductNameAndVariant(String productName, Integer variant);
}
