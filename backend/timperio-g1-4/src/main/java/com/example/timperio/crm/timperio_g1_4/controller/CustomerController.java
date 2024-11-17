package com.example.timperio.crm.timperio_g1_4.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerDto;
import com.example.timperio.crm.timperio_g1_4.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // @PostMapping("/create")
    // public ResponseEntity<CustomerDto> createCustomer(@RequestBody CustomerDto
    // customerDto) {
    // CustomerDto savedCustomer = customerService.createCustomer(customerDto);
    // return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    // }

    @GetMapping("/segmentation-recency")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCustomerListByRecency() {
        try {
            HashMap<String, List<CustomerDto>> customerList = customerService.getCustomerListByRecency();
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has occured.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/segmentation-frequency")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCustomerListByFrequency() {
        try {
            HashMap<String, List<CustomerDto>> customerList = customerService.getCustomerListByFrequency();
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has occured.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/segmentation-spending")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCustomerListBySpending() {
        try {
            HashMap<String, List<CustomerDto>> customerList = customerService.getCustomerListBySpending();
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has occured.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
