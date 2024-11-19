package com.example.timperio.crm.timperio_g1_4.entity;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.timperio.crm.timperio_g1_4.enums.PromotionType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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

    @Column(name = "promotion_name", nullable = false, length = 100)
    private String promotionName;

    @Column(name = "promotion_description", nullable = false, length = 100)
    private String promotionDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "promotion_type", nullable = false, length = 100)
    private PromotionType promotionType;

    @Column(name = "valid_until", nullable = false)
    private LocalDate validUntil;

    @ManyToOne(fetch = FetchType.LAZY, optional = true, cascade = CascadeType.MERGE)
    @JoinColumn(name = "main_product_id", foreignKey = @ForeignKey(name = "FK_PromotionMainProduct"))
    private Product mainProduct; // for discount or related product promotions

    @Column(name = "discount_rate", precision = 10, scale = 2)
    private BigDecimal discountRate; // for discount or related promotions

    @Column(name = "free_quantity")
    private Integer freeQuantity; // for free promotions

    @Column(name = "buy_quantity")
    private Integer buyQuantity; // for free promotions

    @ManyToMany
    @JoinTable(
        name = "promotion_related_products",
        joinColumns = @JoinColumn(name = "promotion_id"),
        inverseJoinColumns = @JoinColumn(name = "related_product_id")
    )
    private List<Product> relatedProducts; // for related product promotions

    @Column(name = "is_frequent_shopper_required")
    private boolean isFrequentShopperRequired; // for related product promotions
}

