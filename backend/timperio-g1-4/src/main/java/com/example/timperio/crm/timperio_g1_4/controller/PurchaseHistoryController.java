package com.example.timperio.crm.timperio_g1_4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.enums.SaleType;
import com.example.timperio.crm.timperio_g1_4.service.impl.PurchaseHistoryServiceImpl;

@RestController
@RequestMapping("/purchase-history")
public class PurchaseHistoryController {
    @Autowired
    private PurchaseHistoryServiceImpl purchaseHistoryService;

    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SaleDto>> getAllPurchaseHistory() {
        return new ResponseEntity<List<SaleDto>>(purchaseHistoryService.getAllPurchaseHistory(), HttpStatus.OK);
    }

    @GetMapping("/customer")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SaleDto>> getCustomerPurchaseHistory(Long customerId) {
        return new ResponseEntity<List<SaleDto>>(purchaseHistoryService.filterByCustomer(customerId), HttpStatus.OK);
    }

    @GetMapping("/sale-type")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SaleDto>> getSaleTypePurchaseHistory(SaleType saleType) {
        return new ResponseEntity<List<SaleDto>>(purchaseHistoryService.filterBySaleType(saleType), HttpStatus.OK);
    }
}
