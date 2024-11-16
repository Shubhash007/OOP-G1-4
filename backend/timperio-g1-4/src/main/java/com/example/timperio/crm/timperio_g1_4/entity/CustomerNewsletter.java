package com.example.timperio.crm.timperio_g1_4.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers_newsletters")
public class CustomerNewsletter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_newsletter_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer; // Links to Customer entity

    @ManyToOne
    @JoinColumn(name = "newsletter_id", nullable = false)
    private Newsletter newsletter; // Links to Newsletter entity

    @Column(name = "send_success", nullable = false)
    private boolean sendSuccess;

    @Column(name = "send_date", nullable = false)
    private LocalDate sendDate;
}
