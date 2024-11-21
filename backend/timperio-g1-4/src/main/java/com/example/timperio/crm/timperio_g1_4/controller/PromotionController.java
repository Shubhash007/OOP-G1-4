package com.example.timperio.crm.timperio_g1_4.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.dto.PromotionDto;
import com.example.timperio.crm.timperio_g1_4.service.PromotionService;

@RestController
@RequestMapping("/promotions")
public class PromotionController {

    @Autowired
    private PromotionService promotionService;

    // endpoint to create or update promotion
    @PostMapping("/create-update")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createOrUpdatePromotion(@RequestBody PromotionDto promotionDto) {
        try {
            PromotionDto savedPromotion = promotionService.createOrUpdatePromotion(promotionDto);
            return new ResponseEntity<>(savedPromotion, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // endpoint to get all promotions
    @GetMapping("/get-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllPromotions() {
        try {
            List<PromotionDto> promotions = promotionService.getAllPromotions();
            return new ResponseEntity<>(promotions, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // endpoint to get a specific promotion by ID
    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getPromotionById(@PathVariable Long id) {
        try {
            PromotionDto promotionDto = promotionService.getPromotionById(id);
            return new ResponseEntity<>(promotionDto, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Promotion not found", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // endpoint to delete multiple promotions by IDs
    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deletePromotions(@RequestBody List<Long> ids) {
        boolean allDeleted = promotionService.deletePromotions(ids);
        if (allDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // 204 No Content
        } else {
            return new ResponseEntity<>(HttpStatus.PARTIAL_CONTENT); // 206 Partial Content
        }
    }
}
