package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.entity.Sale;
import com.example.timperio.crm.timperio_g1_4.enums.SaleType;
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

    public List<SaleDto> filterByCustomer(Long customerId) {
        List<Sale> saleList = saleRepository.findByCustomer_CustomerId(customerId);

        // Map Sales entities to SaleDTOs
        List<SaleDto> saleDtoList = saleList.stream().map(sale -> SaleMapper.mapToSaleDto(sale))
                .collect(Collectors.toList());
        return saleDtoList;
    }

    public List<SaleDto> filterBySaleType(SaleType saleType) {
        List<Sale> saleList = saleRepository.findBySaleType(saleType);

        // Map Sales entities to SaleDTOs
        List<SaleDto> saleDtoList = saleList.stream().map(sale -> SaleMapper.mapToSaleDto(sale))
                .collect(Collectors.toList());
        return saleDtoList;
    }

    public List<SaleDto> filterByValue(Optional<Double> minValue, Optional<Double> maxValue) {
        List<Sale> saleList = saleRepository.findAll();

        // Filter Sales entities by value
        if (minValue.isPresent() || maxValue.isPresent()) {
            saleList = saleList.stream()
                    .filter(sale -> {
                        BigDecimal priceToCompare = sale.getDiscountedPrice() != null ? sale.getDiscountedPrice()
                                : sale.getOriginalPrice();
                        // check against minValue if present
                        boolean isAboveMin = !minValue.isPresent()
                                || priceToCompare.compareTo(BigDecimal.valueOf(minValue.get())) >= 0;
                        // check against maxValue if present
                        boolean isBelowMax = !maxValue.isPresent()
                                || priceToCompare.compareTo(BigDecimal.valueOf(maxValue.get())) <= 0;
                        return isAboveMin && isBelowMax;
                    })
                    .collect(Collectors.toList());
        }

        // Map Sales entities to SaleDTOs
        List<SaleDto> saleDtoList = saleList.stream().map(sale -> SaleMapper.mapToSaleDto(sale))
                .collect(Collectors.toList());
        return saleDtoList;
    }

    public List<SaleDto> filterByDate(Date convertedStartDate, Date convertedEndDate) {
        List<Sale> saleList = saleRepository.findAll();

        // Filter Sales entities by date
        if (convertedStartDate != null || convertedEndDate != null) {
            saleList = saleList.stream()
                    .filter(sale -> {
                        Date saleDate = sale.getSaleDate();
                        // check against startDate if present
                        boolean isAfterStart = convertedStartDate == null
                                || saleDate.compareTo(convertedStartDate) >= 0;
                        // check against endDate if present
                        boolean isBeforeEnd = convertedEndDate == null
                                || saleDate.compareTo(convertedEndDate) <= 0;
                        return isAfterStart && isBeforeEnd;
                    })
                    .collect(Collectors.toList());
        }

        // Map Sales entities to SaleDTOs
        List<SaleDto> saleDtoList = saleList.stream().map(sale -> SaleMapper.mapToSaleDto(sale))
                .collect(Collectors.toList());
        return saleDtoList;
    }
}
