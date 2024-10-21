package com.example.timperio.crm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.dto.SaleDto;
import com.example.timperio.crm.entity.Sale;
import com.example.timperio.crm.mapper.SaleMapper;
import com.example.timperio.crm.repository.SaleRepository;
import com.example.timperio.crm.service.SaleService;

@Service
public class SaleServiceImpl implements SaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Override
    public SaleDto createProduct(SaleDto saleDto) {
        Sale sale = SaleMapper.mapToSale(saleDto);
        Sale savedSale = saleRepository.save(sale);
        return SaleMapper.mapToSaleDto(savedSale);
    }
}
