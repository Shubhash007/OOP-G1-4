package com.example.timperio.crm.timperio_g1_4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.timperio.crm.timperio_g1_4.entity.Sale;
import com.example.timperio.crm.timperio_g1_4.enums.SaleType;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Long> {
    // this naming is necessary to fit the JpaRepository naming convention
    List<Sale> findByCustomer_CustomerId(Long customerId);

    List<Sale> findBySaleType(SaleType saleType);
}
