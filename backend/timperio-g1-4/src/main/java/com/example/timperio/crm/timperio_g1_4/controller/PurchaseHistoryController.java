package com.example.timperio.crm.timperio_g1_4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.entity.Sale;

@RestController
@RequestMapping("/purchase-history")
public class PurchaseHistoryController {
    @Autowired
    private PurchaseHistoryService purchaseHistoryService;

    // test endpoint
    @GetMapping("/all")
    public ResponseEntity<List<Sale>> getAllPurchaseHistory() {
        return new ResponseEntity<>(purchaseHistoryService.getAllPurchaseHistory(), HttpStatus.OK);
    }
}