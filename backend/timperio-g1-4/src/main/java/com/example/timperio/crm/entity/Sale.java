package com.example.timperio.crm.entity;

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

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sales")
public class Sale {

    @Id
    @Column(name = "sale_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long saleId;

    @Column(name = "sale_date")
    private Date saleDate;

    @Column(name = "sale_type")
    private String saleType;

    @Column(name = "digital_channel")
    private String digitalChannel;

    @Column(name = "shipping_method")
    private String shippingMethod;

    // @Column(name = "variant")
    // private String variant;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "product_price")
    private double productPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
}

/*
 * @Id
 * 
 * @Column(name = "row_no")
 * 
 * @GeneratedValue(strategy = GenerationType.IDENTITY)
 * private Long rowNo;
 * 
 * @Column(name = "sale_date")
 * private Date saleDate;
 * 
 * @Column(name = "sale_type")
 * private String saleType;
 * 
 * @Column(name = "digital")
 * private String digital;
 * 
 * @Column(name = "customer_id")
 * private Long customerId;
 * 
 * @Column(name = "zip_code")
 * private Long zipCode;
 * 
 * @Column(name = "shipping_method")
 * private String shippingMethod;
 * 
 * @Column(name = "product")
 * private String product;
 * 
 * @Column(name = "variant")
 * private String variant;
 * 
 * @Column(name = "quantity")
 * private Long quantity;
 * 
 * @Column(name = "price")
 * private double price;
 * 
 * @Column(name = "product_price")
 * private double productPrice;
 */
