package com.example.timperio.crm.timperio_g1_4.service;

import java.util.HashMap;
import java.util.List;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerDto;
import com.example.timperio.crm.timperio_g1_4.entity.Customer;

public interface CustomerService {

    // CustomerDto createCustomer(CustomerDto customerDto);

    List<Customer> getAllCustomers() throws Exception;

    Customer createCustomer(CustomerDto customerDto) throws IllegalArgumentException, Exception;

    Customer updateCustomer(CustomerDto customerDto)
            throws IllegalArgumentException, UsernameNotFoundException, Exception;

    Customer deleteCustomer(Long customerId) throws UsernameNotFoundException, Exception;

    HashMap<String, List<CustomerDto>> getCustomerListByRecency() throws Exception;

    HashMap<String, List<CustomerDto>> getCustomerListByFrequency() throws Exception;

    HashMap<String, List<CustomerDto>> getCustomerListBySpending() throws Exception;
}
