package com.example.timperio.crm.timperio_g1_4.service;

import java.util.List;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.entity.FilterRequest;

public interface PurchaseHistoryService {

    List<SaleDto> getAllPurchaseHistory();

    List<SaleDto> filterPurchaseHistory(FilterRequest filterRequest) throws Exception;
}
