package com.example.timperio.crm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.timperio.crm.dto.CustomerDto;
import com.example.timperio.crm.entity.Customer;
import com.example.timperio.crm.mapper.CustomerMapper;
import com.example.timperio.crm.repository.CustomerRepository;
import com.example.timperio.crm.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    @Transactional
    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = CustomerMapper.mapToCustomer(customerDto);
        // if (customerRepository.existsById(customer.getCustomerId())){
        // return null;
        // }
        Customer savedCustomer = customerRepository.save(customer);
        return CustomerMapper.maptoCustomerDto(savedCustomer);
    }

}
