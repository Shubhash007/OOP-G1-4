package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerDto;
import com.example.timperio.crm.timperio_g1_4.entity.Customer;
import com.example.timperio.crm.timperio_g1_4.mapper.CustomerMapper;
import com.example.timperio.crm.timperio_g1_4.repository.CustomerRepository;
import com.example.timperio.crm.timperio_g1_4.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    // @Override
    // @Transactional
    // public CustomerDto createCustomer(CustomerDto customerDto) {
    // Customer customer = CustomerMapper.mapToCustomer(customerDto);
    // // if (customerRepository.existsById(customer.getCustomerId())){
    // // return null;
    // // }
    // Customer savedCustomer = customerRepository.save(customer);
    // return CustomerMapper.maptoCustomerDto(savedCustomer);
    // }

    public HashMap<String, List<CustomerDto>> getCustomerListByRecency() {
        HashMap<String, List<CustomerDto>> customerList = new HashMap<>();
        // create the 3 categories as such
        List<CustomerDto> activeCustomers = new ArrayList<>();
        List<CustomerDto> dormantCustomers = new ArrayList<>();
        List<CustomerDto> returningCustomers = new ArrayList<>();

        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            CustomerDto customerDto = CustomerMapper.maptoCustomerDto(customer);
            LocalDate lastPurchaseDate = customer.getLastPurchaseDate();

            if (lastPurchaseDate == null) {
                // if the customer has never made a purchase, skip
                continue;
            }
            System.out.println(lastPurchaseDate);
            System.out.println(LocalDate.now().minusDays(30));
            if (lastPurchaseDate.isAfter(LocalDate.now().minusDays(30))) {
                // if the customer's last purchase date is within the last 30 days, active
                // customer
                activeCustomers.add(customerDto);

                if (customer.getReturningCustomer()) {
                    // if the customer is a returning customer
                    returningCustomers.add(customerDto);
                }
            }
            if (lastPurchaseDate.isBefore(LocalDate.now().minusMonths(6))) {
                // if the customer's last purchase date is more than 6 months ago, dormant
                // customer
                dormantCustomers.add(customerDto);
            }
        }

        customerList.put("activeCustomers", activeCustomers);
        customerList.put("dormantCustomers", dormantCustomers);
        customerList.put("returningCustomers", returningCustomers);
        return customerList;
    }
}
