package com.example.timperio.crm.timperio_g1_4.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerDto;
import com.example.timperio.crm.timperio_g1_4.entity.Customer;
import com.example.timperio.crm.timperio_g1_4.mapper.CustomerMapper;
import com.example.timperio.crm.timperio_g1_4.repository.CustomerRepository;
import com.example.timperio.crm.timperio_g1_4.service.CustomerService;


// @Service
// public class CustomerServiceImpl implements CustomerService {

//     @Autowired
//     private CustomerRepository customerRepository;
    
//     @Override
//     @Transactional
//     public CustomerDto createCustomer(CustomerDto customerDto) {
//         Customer customer = CustomerMapper.mapToCustomer(customerDto);
//         // if (customerRepository.existsById(customer.getCustomerId())){
//         //     return null;
//         // }
//         Customer savedCustomer = customerRepository.save(customer);
//         return CustomerMapper.maptoCustomerDto(savedCustomer);
//     }

// }
