package com.example.timperio.crm.timperio_g1_4.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.timperio.crm.timperio_g1_4.service.impl.SalesDataUploadServiceImpl;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/sales-data")
public class SalesDataUploadController {
    @Autowired
    private SalesDataUploadServiceImpl salesDataUploadService;

    @PostMapping("/upload")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> uploadSalesData(@RequestParam("file") MultipartFile file) {
        // validate the file
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            return new ResponseEntity<String>("Please upload a file", HttpStatus.BAD_REQUEST);
        }

        try {
            salesDataUploadService.parseSalesData(file);
            return new ResponseEntity<String>("File uploaded successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>("Error uploading file: " + e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
