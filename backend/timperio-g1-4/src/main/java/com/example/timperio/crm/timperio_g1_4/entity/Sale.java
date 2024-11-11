package com.example.timperio.crm.timperio_g1_4.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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

import java.math.BigDecimal;
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

    @Enumerated(EnumType.STRING)
    @Column(name = "sale_type", nullable = false)
    private SaleType saleType;

    @Enumerated(EnumType.STRING)
    @Column(name = "shipping_method", nullable = false)
    private ShippingMethod shippingMethod;

    @Column(name = "digital")
    private String digital;

    @Column(name = "quantity")
    private Long quantity;

    @Column(name = "original_price", nullable = false, precision = 8, scale = 2)
    private BigDecimal originalPrice;

    @Column(name = "discounted_price", precision = 8, scale = 2)
    private BigDecimal discountedPrice;

    @Column(name = "product_price")
    private double productPrice;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "promotion_id")
    private Promotion promotion;
}

enum ShippingMethod {
    STANDARD_DELIVERY,
    SELF_COLLECT,
    SAME_DAY_DELIVERY
}

enum SaleType {
    DIRECT_B2B,
    CONSIGNMENT,
    MARKETING,
    DIRECT_B2C,
    WHOLESALER
}