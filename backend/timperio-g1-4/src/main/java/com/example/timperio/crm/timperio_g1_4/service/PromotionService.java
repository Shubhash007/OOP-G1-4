package com.example.timperio.crm.timperio_g1_4.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.PromotionDto;

@Service
public interface PromotionService {
    PromotionDto createOrUpdatePromotion(PromotionDto promotionDto) throws IllegalArgumentException, Exception;

    List<PromotionDto> getAllPromotions() throws Exception;

    PromotionDto getPromotionById(Long id) throws NoSuchElementException, Exception;

    boolean deletePromotions(List<Long> ids);

    boolean deletePromotion(Long id) throws NoSuchElementException, Exception;
}