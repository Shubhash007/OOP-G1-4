package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerDto;
import com.example.timperio.crm.timperio_g1_4.entity.Customer;
import com.example.timperio.crm.timperio_g1_4.mapper.CustomerMapper;
import com.example.timperio.crm.timperio_g1_4.repository.CustomerRepository;
import com.example.timperio.crm.timperio_g1_4.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

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

    public HashMap<String, List<CustomerDto>> getCustomerListByFrequency() {
        HashMap<String, List<CustomerDto>> customerList = new HashMap<>();
        // create the 3 categories as such
        List<CustomerDto> frequentCustomers = new ArrayList<>();
        List<CustomerDto> occasionalCustomers = new ArrayList<>();
        List<CustomerDto> rareCustomers = new ArrayList<>();

        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            CustomerDto customerDto = CustomerMapper.maptoCustomerDto(customer);
            int purchaseCount = customer.getPurchaseCount();

            if (purchaseCount == 0) {
                // if the customer has never made a purchase, skip
                continue;
            }
            if (purchaseCount >= 5) {
                // if the customer has made 5 or more purchases, frequent customer
                frequentCustomers.add(customerDto);
            }
            if (purchaseCount >= 2 && purchaseCount < 5) {
                // if the customer has made 2 to 4 purchases, occasional customer
                occasionalCustomers.add(customerDto);
            }
            if (purchaseCount == 1) {
                // if the customer has made only 1 purchase, rare customer
                rareCustomers.add(customerDto);
            }
        }

        customerList.put("frequentCustomers", frequentCustomers);
        customerList.put("occasionalCustomers", occasionalCustomers);
        customerList.put("rareCustomers", rareCustomers);
        return customerList;
    }

    public HashMap<String, List<CustomerDto>> getCustomerListBySpending() {
        HashMap<String, List<CustomerDto>> customerList = new HashMap<>();
        // create the 3 categories as such
        List<Customer> customers = customerRepository.findAll();
        List<Customer> sortedCustomers = new ArrayList<>(customers);
        sortedCustomers.sort((c1, c2) -> c2.getTotalExpenditure().compareTo(c1.getTotalExpenditure()));

        int top10Percent = (int) Math.ceil(customers.size() * 0.1);
        int bottom20Percent = (int) Math.floor(customers.size() * 0.2);

        // top 10% lifetime spending
        List<CustomerDto> highValueCustomers = sortedCustomers.subList(0, top10Percent).stream()
                .map(CustomerMapper::maptoCustomerDto).collect(Collectors.toList());

        // 10th-80th percentile lifetime spending
        List<CustomerDto> midTierCustomers = sortedCustomers
                .subList(top10Percent, customers.size() - bottom20Percent)
                .stream().map(CustomerMapper::maptoCustomerDto).collect(Collectors.toList());

        // bottom 20% lifetime spending
        List<CustomerDto> lowSpendCustomers = sortedCustomers
                .subList(customers.size() - bottom20Percent, customers.size())
                .stream().map(CustomerMapper::maptoCustomerDto).collect(Collectors.toList());

        customerList.put("High-Value Customers", highValueCustomers);
        customerList.put("Mid-Tier Customers", midTierCustomers);
        customerList.put("Low-Spend Customers", lowSpendCustomers);

        return customerList;
    }
}
