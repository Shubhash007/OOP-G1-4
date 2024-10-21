package com.example.timperio.crm.entity;

import java.util.ArrayList;
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
    // #@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long customerId;

    @ElementCollection
    @CollectionTable(name = "customer_zip_codes", joinColumns = @JoinColumn(name = "customer_id"))
    @Column(name = "zip_code")
    private List<Long> zipCode; // multiple addresses Integer array

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    @Column(nullable = true)
    private List<Sale> sales = new ArrayList<>();
}
