package com.example.timperio.crm.timperio_g1_4.mapper;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerDto;
import com.example.timperio.crm.timperio_g1_4.entity.Customer;

public class CustomerMapper {

    // changed this to only return data necessary for customer segmentation list
    public static CustomerDto maptoCustomerDto(Customer customer) {
        return new CustomerDto(
                customer.getCustomerId(),
                customer.getZipCodes(),
                customer.getLastPurchaseDate(),
                customer.getAcceptNewsletter(),
                customer.getEmail(),
                customer.getReturningCustomer(),
                customer.getPurchaseCount(),
                customer.getTotalExpenditure());
    }

    // public static Customer mapToCustomer(CustomerDto customerDto){
    // return new Customer(
    // customerDto.getCustomerId(),
    // customerDto.getZipCode(),
    // customerDto.getSales()
    // );
    // }
}
