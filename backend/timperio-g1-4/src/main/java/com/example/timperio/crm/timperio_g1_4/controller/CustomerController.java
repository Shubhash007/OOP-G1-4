package com.example.timperio.crm.timperio_g1_4.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerDto;
import com.example.timperio.crm.timperio_g1_4.entity.Customer;
import com.example.timperio.crm.timperio_g1_4.service.CustomerService;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    // get all customers
    @GetMapping("/all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllCustomers() {
        try {
            List<Customer> customers = customerService.getAllCustomers();
            return new ResponseEntity<>(customers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has occured.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // create a customer
    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_SALES'), hasRole('ROLE_MARKETING')")
    public ResponseEntity<?> createCustomer(@RequestBody CustomerDto customerDto) {
        try {
            Customer createdCustomer = customerService.createCustomer(customerDto);
            return new ResponseEntity<>(createdCustomer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has occured.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // update a customer
    // the only fields that should be updatable are the zip code, email, and
    // newsletter subscription
    @PutMapping("/update")
    @PreAuthorize("hasRole('ROLE_SALES'), hasRole('ROLE_MARKETING')")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerDto customerDto) {
        try {
            Customer updatedCustomer = customerService.updateCustomer(customerDto);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // delete a customer by id
    @DeleteMapping("/delete")
    @PreAuthorize("hasRole('ROLE_SALES'), hasRole('ROLE_MARKETING')")
    public ResponseEntity<?> deleteCustomer(@RequestParam Long customerId) {
        try {
            customerService.deleteCustomer(customerId);
            return new ResponseEntity<>("Customer with id " + customerId + " has been deleted.",
                    HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has occured.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/segmentation/recency")
    @PreAuthorize("hasRole('ROLE_MARKETING')")
    public ResponseEntity<?> getCustomerListByRecency() {
        try {
            HashMap<String, List<CustomerDto>> customerList = customerService.getCustomerListByRecency();
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has occured.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/segmentation/frequency")
    @PreAuthorize("hasRole('ROLE_MARKETING')")
    public ResponseEntity<?> getCustomerListByFrequency() {
        try {
            HashMap<String, List<CustomerDto>> customerList = customerService.getCustomerListByFrequency();
            return new ResponseEntity<>(customerList, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("An internal error has occured.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/segmentation/spending")
    @PreAuthorize("hasRole('ROLE_MARKETING')")
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
