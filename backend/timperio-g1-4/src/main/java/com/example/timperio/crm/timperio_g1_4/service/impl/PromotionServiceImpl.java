package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.PromotionDto;
import com.example.timperio.crm.timperio_g1_4.entity.Product;
import com.example.timperio.crm.timperio_g1_4.entity.Promotion;
import com.example.timperio.crm.timperio_g1_4.mapper.PromotionMapper;
import com.example.timperio.crm.timperio_g1_4.repository.ProductRepository;
import com.example.timperio.crm.timperio_g1_4.repository.PromotionRepository;
import com.example.timperio.crm.timperio_g1_4.service.PromotionService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PromotionServiceImpl implements PromotionService {

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductRepository productRepository;

    // method to create or update promotion
    public PromotionDto createOrUpdatePromotion(PromotionDto promotionDto) throws IllegalArgumentException, Exception {
        try {
            Promotion promotion = PromotionMapper.mapToEntity(promotionDto);

            // Handle main product if applicable
            if (promotionDto.getMainProductId() != null) {
                Product mainProduct = productRepository.findById(promotionDto.getMainProductId())
                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));

                promotion.setMainProduct(mainProduct);

                promotion.setMainProduct(mainProduct);
            } else {
                throw new IllegalArgumentException("Please choose a product for the promotion.");
            }

            // Handle related products if applicable
            if (promotionDto.getRelatedProductIds() != null && !promotionDto.getRelatedProductIds().isEmpty()) {
                promotion.setRelatedProducts(
                        promotionDto.getRelatedProductIds().stream()
                                .map(productId -> productRepository.findById(productId).orElse(null))
                                .collect(Collectors.toList()));
            }

            // Save the promotion
            promotion = promotionRepository.save(promotion);

            // Map entity back to DTO
            return PromotionMapper.mapToDto(promotion);
        } catch (IllegalArgumentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error occured.");
        }

    }

    // method to get all promotions
    public List<PromotionDto> getAllPromotions() throws Exception {
        try {
            return promotionRepository.findAll().stream()
                    .map(PromotionMapper::mapToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error occured.");
        }

    }

    // method to get promotion by id
    public PromotionDto getPromotionById(Long id) throws NoSuchElementException, Exception {
        try {
            Optional<Promotion> promotion = promotionRepository.findById(id);
            if (promotion.isPresent()) {
                return PromotionMapper.mapToDto(promotion.get());
            } else {
                throw new NoSuchElementException("Promotion not found");
            }
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error occured.");
        }
    }

    // method to delete multiple promotions by array of ids
    public boolean deletePromotions(List<Long> ids) {
        try {
            boolean allDeleted = true;
            for (Long id : ids) {
                boolean isDeleted = deletePromotion(id); // Call the existing delete logic for each ID
                if (!isDeleted) {
                    allDeleted = false; // If any deletion fails, mark as partial success
                }
            }
            return allDeleted;
        } catch (Exception e) {
            // for this case, because we allow partial delete, we do not throw error
            e.printStackTrace();
        }
        return false;
    }

    // method to delete a single promotion
    public boolean deletePromotion(Long id) throws Exception {
        try {
            Optional<Promotion> promotion = promotionRepository.findById(id);
            if (promotion.isPresent()) {
                promotionRepository.delete(promotion.get());
                return true;
            } else {
                throw new NoSuchElementException("Promotion not found");
            }
        } catch (NoSuchElementException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error occured.");
        }
    }

}
