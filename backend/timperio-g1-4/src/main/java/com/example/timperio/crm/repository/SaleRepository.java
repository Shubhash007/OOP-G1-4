package com.example.timperio.crm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.timperio.crm.entity.Sale;

public interface SaleRepository extends JpaRepository<Sale, Long> {

}
