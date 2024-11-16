package com.example.timperio.crm.timperio_g1_4.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.enums.SaleType;

public interface PurchaseHistoryService {

    List<SaleDto> getAllPurchaseHistory();

    List<SaleDto> filterByCustomer(Long customerId);

    List<SaleDto> filterBySaleType(SaleType saleType);

    List<SaleDto> filterByValue(Optional<Double> minValue, Optional<Double> maxValue);

    List<SaleDto> filterByDate(Date startDate, Date endDate);
}
