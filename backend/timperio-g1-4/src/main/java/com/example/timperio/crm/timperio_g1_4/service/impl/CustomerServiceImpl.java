package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerDto;
import com.example.timperio.crm.timperio_g1_4.entity.Customer;
import com.example.timperio.crm.timperio_g1_4.entity.Sale;
import com.example.timperio.crm.timperio_g1_4.mapper.CustomerMapper;
import com.example.timperio.crm.timperio_g1_4.repository.CustomerRepository;
import com.example.timperio.crm.timperio_g1_4.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() throws Exception {
        try {
            return customerRepository.findAll();
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error has occured.");
        }
    }

    public Customer createCustomer(CustomerDto customerDto) throws IllegalArgumentException, Exception {
        try {
            Customer customer = new Customer();
            customer.setZipCodes(customerDto.getZipCode());
            customer.setAcceptNewsletter(customerDto.getAcceptNewsletter());
            customer.setEmail(customerDto.getEmail());
            customer.setLastPurchaseDate(customerDto.getLastPurchaseDate());
            customer.setReturningCustomer(customerDto.getReturningCustomer());
            customer.setPurchaseCount(customerDto.getPurchaseCount());
            customer.setTotalExpenditure(customerDto.getTotalExpenditure());
            return customerRepository.save(customer);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input for customer creation.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error has occured.");
        }

    }

    public Customer updateCustomer(CustomerDto customerDto)
            throws IllegalArgumentException, UsernameNotFoundException, Exception {
        try {
            if (customerDto.getCustomerId() == null) {
                throw new IllegalArgumentException("Please include the id of the customer to update.");
            }
            Customer customer = customerRepository.findById(customerDto.getCustomerId())
                    .orElseThrow(() -> new UsernameNotFoundException(
                            "Unable to find customer with id " + customerDto.getCustomerId()));
            customer.setZipCodes(customerDto.getZipCode());
            customer.setAcceptNewsletter(customerDto.getAcceptNewsletter());
            customer.setEmail(customerDto.getEmail());
            return customerRepository.save(customer);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid input for customer update.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error has occured.");
        }
    }

    public Customer deleteCustomer(Long customerId) throws UsernameNotFoundException, Exception {
        try {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new UsernameNotFoundException("Unable to find customer with id " + customerId));
            customerRepository.delete(customer);
            return customer;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error has occured.");
        }
    }

    public HashMap<String, List<CustomerDto>> getCustomerListByRecency() throws Exception {
        HashMap<String, List<CustomerDto>> customerList = new HashMap<>();
        // create the 3 categories as such
        List<CustomerDto> activeCustomers = new ArrayList<>();
        List<CustomerDto> dormantCustomers = new ArrayList<>();
        List<CustomerDto> returningCustomers = new ArrayList<>();

        try {
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

            customerList.put("Active Customer", activeCustomers);
            customerList.put("Dormant Customers", dormantCustomers);
            customerList.put("Returning Customers", returningCustomers);
            return customerList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error has occured.");
        }

    }

    public HashMap<String, List<CustomerDto>> getCustomerListByFrequency() throws Exception {
        HashMap<String, List<CustomerDto>> customerList = new HashMap<>();
        // create the 3 categories as such
        List<CustomerDto> frequentCustomers = new ArrayList<>();
        List<CustomerDto> occasionalCustomers = new ArrayList<>();
        List<CustomerDto> oneTimeCustomers = new ArrayList<>();

        try {
            List<Customer> customers = customerRepository.findAll();
            for (Customer customer : customers) {
                CustomerDto customerDto = CustomerMapper.maptoCustomerDto(customer);
                int purchaseCount = customer.getPurchaseCount();

                if (purchaseCount == 0) {
                    // if the customer has never made a purchase, skip
                    continue;
                }
                int monthCount = 0;
                int quarterCount = 0;
                // retrieve the sales for the customer
                for (Sale sale : customer.getSales()) {
                    LocalDate date = sale.getSaleDate();

                    if (date.isAfter(LocalDate.now().minusMonths(1))) {
                        // if the sale is within the last 6 months, increment the purchase count
                        monthCount++;
                    }
                    if (date.isAfter(LocalDate.now().minusMonths(3))) {
                        // if the sale is within the last 3 months, increment the purchase count
                        quarterCount++;
                    }
                }

                if (purchaseCount == 1) {
                    // if the customer has made only 1 purchase, one-time buyer
                    oneTimeCustomers.add(customerDto);
                }
                if (monthCount >= 10) {
                    // if the customer has made 10 or more purchases in the last month, frequent
                    // shopper
                    frequentCustomers.add(customerDto);
                }
                if (quarterCount >= 3 && quarterCount <= 5) {
                    // if the customer has made 3-5 purchases in the last quarter, occasional
                    // shopper
                    occasionalCustomers.add(customerDto);
                }
            }

            customerList.put("Frequent Shoppers", frequentCustomers);
            customerList.put("Occasional Shoppers", occasionalCustomers);
            customerList.put("One-Time Buyers", oneTimeCustomers);
            return customerList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error has occured.");
        }
    }

    public HashMap<String, List<CustomerDto>> getCustomerListBySpending() throws Exception {
        HashMap<String, List<CustomerDto>> customerList = new HashMap<>();
        // create the 3 categories as such
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error has occured.");
        }
    }
}
