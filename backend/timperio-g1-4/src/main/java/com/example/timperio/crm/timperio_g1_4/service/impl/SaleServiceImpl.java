package com.example.timperio.crm.timperio_g1_4.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.entity.Sale;
import com.example.timperio.crm.timperio_g1_4.mapper.SaleMapper;
import com.example.timperio.crm.timperio_g1_4.repository.SaleRepository;
import com.example.timperio.crm.timperio_g1_4.service.SaleService;

// @Service
// public class SaleServiceImpl implements SaleService {

//     @Autowired
//     private SaleRepository saleRepository;

//     @Override
//     public SaleDto createProduct(SaleDto saleDto){
//         Sale sale = SaleMapper.mapToSale(saleDto);
//         Sale savedSale = saleRepository.save(sale);
//         return SaleMapper.mapToSaleDto(savedSale);
//     }
// }
