package com.example.timperio.crm.timperio_g1_4.controller;

import java.io.IOException;
import org.springframework.http.HttpHeaders;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.entity.FilterRequest;
import com.example.timperio.crm.timperio_g1_4.service.impl.PurchaseHistoryServiceImpl;
import com.example.timperio.crm.utils.PurchaseHistoryCSVExporter;

import jakarta.servlet.http.HttpServletResponse;

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

    @PostMapping("/filter")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> filterPurchaseHistory(@RequestBody FilterRequest filterRequest) throws Exception {
        try {
            return new ResponseEntity<List<SaleDto>>(purchaseHistoryService.filterPurchaseHistory(filterRequest),
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/export-csv")
    @PreAuthorize("isAuthenticated()")
    public void exportToCSV(@RequestBody FilterRequest filterRequest, HttpServletResponse response)
            throws IOException, Exception {
        List<SaleDto> purchaseHistoryList;

        try {
            purchaseHistoryList = purchaseHistoryService.filterPurchaseHistory(filterRequest);
        } catch (Exception e) {
            throw new Exception("Something went wrong with the Purchase History Filter.");
        }

        // set response headers
        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sales_data.csv");

        // Write CSV to response output stream
        PurchaseHistoryCSVExporter.exportToCSV(response.getWriter(), purchaseHistoryList);
    }
}
