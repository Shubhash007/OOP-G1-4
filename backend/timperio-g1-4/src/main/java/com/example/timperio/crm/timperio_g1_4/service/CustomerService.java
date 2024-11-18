package com.example.timperio.crm.timperio_g1_4.service;

import java.util.HashMap;
import java.util.List;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerDto;
import com.example.timperio.crm.timperio_g1_4.entity.Customer;

public interface CustomerService {

    // CustomerDto createCustomer(CustomerDto customerDto);

    List<Customer> getAllCustomers();

    Customer createCustomer(CustomerDto customerDto);

    Customer updateCustomer(CustomerDto customerDto) throws Exception;

    HashMap<String, List<CustomerDto>> getCustomerListByRecency();

    HashMap<String, List<CustomerDto>> getCustomerListByFrequency();

    HashMap<String, List<CustomerDto>> getCustomerListBySpending();
}
