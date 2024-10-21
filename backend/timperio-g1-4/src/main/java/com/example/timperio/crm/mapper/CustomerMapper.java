package com.example.timperio.crm.mapper;

import com.example.timperio.crm.dto.CustomerDto;
import com.example.timperio.crm.entity.Customer;

public class CustomerMapper {

    public static CustomerDto maptoCustomerDto(Customer customer) {
        return new CustomerDto(
                customer.getCustomerId(),
                customer.getZipCode(),
                customer.getSales());
    }

    public static Customer mapToCustomer(CustomerDto customerDto) {
        return new Customer(
                customerDto.getCustomerId(),
                customerDto.getZipCode(),
                customerDto.getSales());
    }
}
