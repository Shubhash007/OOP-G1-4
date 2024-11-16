package com.example.timperio.crm.timperio_g1_4.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.PromotionDto;
import com.example.timperio.crm.timperio_g1_4.entity.Product;
import com.example.timperio.crm.timperio_g1_4.entity.Promotion;
import com.example.timperio.crm.timperio_g1_4.repository.ProductRepository;
import com.example.timperio.crm.timperio_g1_4.repository.PromotionRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductRepository productRepository;

    // method to create or update promotion
    public PromotionDto createOrUpdatePromotion(PromotionDto promotionDto) {
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

        // Handle main product if applicable
        if (promotionDto.getMainProductId() != null) {
            Product mainProduct = productRepository.findById(promotionDto.getMainProductId())
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            
            promotion.setMainProduct(mainProduct);

    promotion.setMainProduct(mainProduct);        }
        else{
            throw new IllegalArgumentException("Please choose a product for the promotion.");
        }

        // Handle related products if applicable
        if (promotionDto.getRelatedProductIds() != null && !promotionDto.getRelatedProductIds().isEmpty()) {
            promotion.setRelatedProducts(
                promotionDto.getRelatedProductIds().stream()
                    .map(productId -> productRepository.findById(productId).orElse(null))
                    .collect(Collectors.toList())
            );
        }

        // Save the promotion
        promotion = promotionRepository.save(promotion);

        // Map entity back to DTO
        return mapToDto(promotion);
    }

    // method to get all promotions
    public List<PromotionDto> getAllPromotions() {
        return promotionRepository.findAll().stream()
            .map(this::mapToDto)
            .collect(Collectors.toList());
    }

    // method to get promotion by id
    public PromotionDto getPromotionById(Long id) {
        Optional<Promotion> promotion = promotionRepository.findById(id);

        if (promotion.isPresent()) {
            return mapToDto(promotion.get());
        } else {
            return null; // or throw exception if you prefer
        }
    }

    // method to map entity to DTO
    private PromotionDto mapToDto(Promotion promotion) {
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
                    .collect(Collectors.toList())
            );
        }

        return promotionDto;
    }
}
