package com.example.timperio.crm.timperio_g1_4.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.entity.SalesMetrics;
import com.example.timperio.crm.timperio_g1_4.service.impl.SalesMetricsServiceImpl;

@RestController
@PreAuthorize("hasRole('ROLE_SALES')")
@RequestMapping("/sales-metrics")
public class SalesMetricsController {
    @Autowired
    private SalesMetricsServiceImpl salesMetricsService;

    @GetMapping("/get")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getSalesMetrics(boolean individual, Optional<Long> customerId)
            throws IllegalArgumentException {
        // individual should be true if we want to get the sales metrics for a specific
        // customer
        try {
            return new ResponseEntity<SalesMetrics>(
                    salesMetricsService.getSalesMetrics(individual, customerId),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
