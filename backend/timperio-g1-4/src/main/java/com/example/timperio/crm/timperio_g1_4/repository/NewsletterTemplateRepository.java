package com.example.timperio.crm.timperio_g1_4.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.timperio.crm.timperio_g1_4.entity.NewsletterTemplate;

@Repository
public interface NewsletterTemplateRepository extends JpaRepository<NewsletterTemplate, Long> {
}
