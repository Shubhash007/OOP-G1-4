package com.example.timperio.crm.timperio_g1_4.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.timperio.crm.timperio_g1_4.entity.CustomerNewsletter;

@Repository
public interface CustomerNewsletterRepository extends JpaRepository<CustomerNewsletter, Long> {

    List<CustomerNewsletter> findByCustomer_CustomerId(Long customerId);

    List<CustomerNewsletter> findByNewsletter_Id(Long newsletterId);
}
