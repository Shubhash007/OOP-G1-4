package com.example.timperio.crm.timperio_g1_4.service;

import java.util.HashMap;
import java.util.List;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerDto;

public interface CustomerService {

    // CustomerDto createCustomer(CustomerDto customerDto);

    HashMap<String, List<CustomerDto>> getCustomerListByRecency();
}
