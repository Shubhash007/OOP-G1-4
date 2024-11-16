package com.example.timperio.crm.timperio_g1_4.controller;

import java.text.SimpleDateFormat;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

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

    @GetMapping("/value")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SaleDto>> getValuePurchaseHistory(Optional<Double> minValue, Optional<Double> maxValue) {
        return new ResponseEntity<List<SaleDto>>(purchaseHistoryService.filterByValue(minValue, maxValue),
                HttpStatus.OK);
    }

    @GetMapping("/date")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getDatePurchaseHistory(Optional<String> startDate, Optional<String> endDate) {
        // convert strings to date
        Date convertedStartDate = null;
        Date convertedEndDate = null;

        if (startDate.isPresent() && endDate.isPresent()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                convertedStartDate = new Date(sdf.parse(startDate.get()).getTime());
                convertedEndDate = new Date(sdf.parse(endDate.get()).getTime());
            } catch (Exception e) {
                return new ResponseEntity<String>("Invalid date format", HttpStatus.BAD_REQUEST);
            }
        }

        return new ResponseEntity<List<SaleDto>>(
                purchaseHistoryService.filterByDate(convertedStartDate, convertedEndDate),
                HttpStatus.OK);
    }
}
