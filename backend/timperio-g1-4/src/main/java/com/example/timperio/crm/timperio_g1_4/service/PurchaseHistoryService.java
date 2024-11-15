package com.example.timperio.crm.timperio_g1_4.service;

import java.util.List;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.enums.SaleType;

public interface PurchaseHistoryService {

    List<SaleDto> getAllPurchaseHistory();

    List<SaleDto> filterByCustomer(Long customerId);

    List<SaleDto> filterBySaleType(SaleType saleType);
}
