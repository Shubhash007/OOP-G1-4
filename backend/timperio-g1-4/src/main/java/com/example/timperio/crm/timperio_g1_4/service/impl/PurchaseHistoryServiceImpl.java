package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
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

    public List<SaleDto> filterByCustomer(Long customerId) {
        List<Sale> saleList = saleRepository.findByCustomer_CustomerId(customerId);

        // Map Sales entities to SaleDTOs
        List<SaleDto> saleDtoList = saleList.stream().map(sale -> SaleMapper.mapToSaleDto(sale))
                .collect(Collectors.toList());
        return saleDtoList;
    }
}
