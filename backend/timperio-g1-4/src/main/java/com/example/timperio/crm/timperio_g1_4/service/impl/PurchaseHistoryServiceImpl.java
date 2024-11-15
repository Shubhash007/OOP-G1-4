package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.entity.Sale;
import com.example.timperio.crm.timperio_g1_4.repository.SaleRepository;
import com.example.timperio.crm.timperio_g1_4.service.PurchaseHistoryService;

@Service
public class PurchaseHistoryServiceImpl implements PurchaseHistoryService {

    @Autowired
    private SaleRepository saleRepository;

    public List<Sale> getAllPurchaseHistory() {
        return saleRepository.findAll();
    }
}
