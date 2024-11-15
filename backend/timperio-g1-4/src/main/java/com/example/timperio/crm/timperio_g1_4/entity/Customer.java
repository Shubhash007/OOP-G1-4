package com.example.timperio.crm.timperio_g1_4.entity;

import java.time.LocalDate;
import java.util.ArrayList;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "customers")
public class Customer {

    // private Long id;

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @ElementCollection
    @CollectionTable(name = "customer_zip_codes", joinColumns = @JoinColumn(name = "customer_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "customer_id", "zip_code" }))
    @Column(name = "zip_code")
    private List<Long> zipCode = new ArrayList<>(); // multiple addresses Integer array

    @Column(name = "last_purchase_date")
    private LocalDate lastPurchaseDate;

    @Column(name = "accept_newsletter", nullable = false)
    private Boolean acceptNewsletter = false;

    @Column(name = "email", length = 100)
    private String email;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @JsonIgnore
    @Column(nullable = true)
    private List<Sale> sales = new ArrayList<>();
}
