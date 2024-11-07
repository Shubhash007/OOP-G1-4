package com.example.timperio.crm.timperio_g1_4.entity;
import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "promotions")
public class Promotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promotion_id")
    private Long promotionId;

    @Column(name = "promotion_name", nullable = false, length = 100, unique = true)
    private String promotionName;

    @Column(name = "promotion_description", nullable = false, length = 100)
    private String promotionDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", nullable = false, length = 100)
    private PromotionType promotionType;

    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    // @ManyToOne(fetch = FetchType.LAZY, optional = false)
    // @JoinColumn(name = "main_product_id", nullable = false, foreignKey = @ForeignKey(name = "FK_PromotionMainProduct"))
    // private Product mainProduct;

    @Column(name = "discount_rate", precision = 10, scale = 2)
    private BigDecimal discountRate;

    @Column(name = "free_quantity")
    private Integer freeQuantity;

    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "promo_product_id", foreignKey = @ForeignKey(name = "FK_PromotionPromoProduct"))
    // private Product promoProduct;

    // Optional: Add relationships if Promotion is referenced elsewhere
}
    
enum PromotionType {
    SEASONAL,
    CLEARANCE,
    NEW_ARRIVAL,
    BUNDLE,
    FLASH_SALE
}

