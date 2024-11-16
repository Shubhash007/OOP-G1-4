package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.MappingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.entity.FilterRequest;
import com.example.timperio.crm.timperio_g1_4.entity.Sale;
import com.example.timperio.crm.timperio_g1_4.mapper.SaleMapper;
import com.example.timperio.crm.timperio_g1_4.repository.SaleRepository;
import com.example.timperio.crm.timperio_g1_4.service.PurchaseHistoryService;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {

    @Autowired
    private SaleRepository saleRepository;

    public List<SaleDto> getAllPurchaseHistory() {
        List<Sale> saleList = saleRepository.findAll();

        // Map Sales entities to SaleDTOs
        List<SaleDto> saleDtoList = saleList.stream().map(sale -> SaleMapper.mapToSaleDto(sale))
                .collect(Collectors.toList());
        return saleDtoList;
    }

    public List<SaleDto> filterPurchaseHistory(FilterRequest filterRequest) throws Exception {
        List<Sale> saleList;

        // Filter Sales entities by customer, sale type, value, and date
        // if customerid is present, we only retrieve the sales for that customer
        // else we retrieve all sales first
        try {
            if (filterRequest.getCustomerId().isPresent()) {
                saleList = saleRepository.findByCustomer_CustomerId(filterRequest.getCustomerId().get());
            } else {
                saleList = saleRepository.findAll();
            }
        } catch (DataAccessException e) {
            throw new Exception("Error retrieving sales data");
        }

        try {
            // filter by SaleType
            if (filterRequest.getSaleType().isPresent()) {
                saleList = saleList.stream()
                        .filter(sale -> sale.getSaleType().equals(filterRequest.getSaleType().get()))
                        .collect(Collectors.toList());
            }

            // filter by value
            if (filterRequest.getMinValue().isPresent() || filterRequest.getMaxValue().isPresent()) {
                saleList = saleList.stream()
                        .filter(sale -> {
                            BigDecimal priceToCompare = sale.getDiscountedPrice() != null ? sale.getDiscountedPrice()
                                    : sale.getOriginalPrice();
                            // check against minValue if present
                            boolean isAboveMin = !filterRequest.getMinValue().isPresent()
                                    || priceToCompare
                                            .compareTo(BigDecimal.valueOf(filterRequest.getMinValue().get())) >= 0;
                            // check against maxValue if present
                            boolean isBelowMax = !filterRequest.getMaxValue().isPresent()
                                    || priceToCompare
                                            .compareTo(BigDecimal.valueOf(filterRequest.getMaxValue().get())) <= 0;
                            return isAboveMin && isBelowMax;
                        })
                        .collect(Collectors.toList());
            }

            // filter by date
            if (filterRequest.getStartDate().isPresent() || filterRequest.getEndDate().isPresent()) {
                Date convertedStartDate = null;
                Date convertedEndDate = null;
                // Convert start and end dates if present
                if (filterRequest.getStartDate().isPresent()) {
                    convertedStartDate = Date.valueOf(filterRequest.getStartDate().get());
                }

                if (filterRequest.getEndDate().isPresent()) {
                    convertedEndDate = Date.valueOf(filterRequest.getEndDate().get());
                }

                final Date startDate = convertedStartDate;
                final Date endDate = convertedEndDate;

                // Filter sales within the date range
                saleList = saleList.stream()
                        .filter(sale -> {
                            Date saleDate = sale.getSaleDate();
                            // Keep only sales within the date range
                            boolean isAfterStart = startDate == null || !saleDate.before(startDate); // Use `before` for
                                                                                                     // clarity
                            boolean isBeforeEnd = endDate == null || !saleDate.after(endDate); // Use `after` for
                                                                                               // clarity
                            return isAfterStart && isBeforeEnd;
                        })
                        .collect(Collectors.toList());
            }
        } catch (NumberFormatException e) {
            throw new Exception("There was an issue with the minValue or maxValue provided.");
        } catch (IllegalArgumentException e) {
            throw new Exception("Invalid date format. Expected format is YYYY-MM-DD.");
        } catch (NullPointerException e) {
            throw new Exception("Fully omit the fields which are not to be filtered.");
        }

        List<SaleDto> saleDtoList;
        try {
            // Map Sales entities to SaleDTOs
            saleDtoList = saleList.stream().map(sale -> SaleMapper.mapToSaleDto(sale))
                    .collect(Collectors.toList());
        } catch (MappingException e) {
            throw new Exception("Error mapping sales data to DTOs");
        }

        return saleDtoList;
    }
}
