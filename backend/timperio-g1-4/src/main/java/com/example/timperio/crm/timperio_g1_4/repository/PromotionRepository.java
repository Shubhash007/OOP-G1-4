package com.example.timperio.crm.timperio_g1_4.repository;

import com.example.timperio.crm.timperio_g1_4.entity.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
    // custom queries can be added if needed
}
