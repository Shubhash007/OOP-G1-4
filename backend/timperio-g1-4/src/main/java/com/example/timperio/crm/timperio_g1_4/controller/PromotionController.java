package com.example.timperio.crm.timperio_g1_4.controller;

import com.example.timperio.crm.timperio_g1_4.dto.PromotionDto;
import com.example.timperio.crm.timperio_g1_4.service.PromotionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    // endpoint to create or update promotion
    @PostMapping
    public ResponseEntity<PromotionDto> createOrUpdatePromotion(@RequestBody PromotionDto promotionDto) {
        PromotionDto savedPromotion = promotionService.createOrUpdatePromotion(promotionDto);
        return new ResponseEntity<>(savedPromotion, HttpStatus.CREATED);
    }

    // endpoint to get all promotions
    @GetMapping
    public ResponseEntity<List<PromotionDto>> getAllPromotions() {
        List<PromotionDto> promotions = promotionService.getAllPromotions();
        return new ResponseEntity<>(promotions, HttpStatus.OK);
    }

    // endpoint to get a specific promotion by ID
    @GetMapping("/{id}")
    public ResponseEntity<PromotionDto> getPromotionById(@PathVariable Long id) {
        // assuming a method in service that returns a promotion by id
        PromotionDto promotionDto = promotionService.getPromotionById(id);
        if (promotionDto != null) {
            return new ResponseEntity<>(promotionDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
