package com.example.timperio.crm.timperio_g1_4.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import com.example.timperio.crm.timperio_g1_4.entity.Product;
import com.example.timperio.crm.timperio_g1_4.enums.PromotionType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewsletterPromotionDto {
    private Long promotionId; // for updates
    private String promotionName;
    private String promotionDescription;
    private PromotionType promotionType;
    private LocalDate validUntil;
    private ProductDto mainProduct; // id of the main product
    private BigDecimal discountRate;
    private Integer freeQuantity;
    private Integer buyQuantity;
    private List<ProductDto> relatedProducts; // ids of related products
    private boolean frequentShopperRequired;
}
