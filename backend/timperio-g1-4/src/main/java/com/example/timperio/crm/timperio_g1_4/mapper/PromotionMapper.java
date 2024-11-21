package com.example.timperio.crm.timperio_g1_4.mapper;

import java.util.stream.Collectors;

import com.example.timperio.crm.timperio_g1_4.dto.PromotionDto;
import com.example.timperio.crm.timperio_g1_4.entity.Product;
import com.example.timperio.crm.timperio_g1_4.entity.Promotion;

import jakarta.persistence.EntityNotFoundException;

public class PromotionMapper {
    // method to map entity to DTO
    public static PromotionDto mapToDto(Promotion promotion) {
        PromotionDto promotionDto = new PromotionDto();
        promotionDto.setPromotionId(promotion.getPromotionId());
        promotionDto.setPromotionName(promotion.getPromotionName());
        promotionDto.setPromotionDescription(promotion.getPromotionDescription());
        promotionDto.setPromotionType(promotion.getPromotionType());
        promotionDto.setValidUntil(promotion.getValidUntil());
        promotionDto.setDiscountRate(promotion.getDiscountRate());
        promotionDto.setFreeQuantity(promotion.getFreeQuantity());
        promotionDto.setBuyQuantity(promotion.getBuyQuantity());
        promotionDto.setFrequentShopperRequired(promotion.isFrequentShopperRequired());

        // Set main product id
        if (promotion.getMainProduct() != null) {
            promotionDto.setMainProductId(promotion.getMainProduct().getProductId());
        }

        // Set related product ids
        if (promotion.getRelatedProducts() != null) {
            promotionDto.setRelatedProductIds(
                    promotion.getRelatedProducts().stream()
                            .map(product -> product.getProductId())
                            .collect(Collectors.toList()));
        }

        return promotionDto;
    }

    public static Promotion mapToEntity(PromotionDto promotionDto) {
        Promotion promotion = new Promotion();

        // mapping DTO to Entity
        promotion.setPromotionId(promotionDto.getPromotionId());
        promotion.setPromotionName(promotionDto.getPromotionName());
        promotion.setPromotionDescription(promotionDto.getPromotionDescription());
        promotion.setPromotionType(promotionDto.getPromotionType());
        promotion.setValidUntil(promotionDto.getValidUntil());
        promotion.setDiscountRate(promotionDto.getDiscountRate());
        promotion.setFreeQuantity(promotionDto.getFreeQuantity());
        promotion.setBuyQuantity(promotionDto.getBuyQuantity());
        promotion.setFrequentShopperRequired(promotionDto.isFrequentShopperRequired());

        return promotion;
    }
}
