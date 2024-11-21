package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.entity.Sale;
import com.example.timperio.crm.timperio_g1_4.entity.SalesMetrics;
import com.example.timperio.crm.timperio_g1_4.enums.SaleType;
import com.example.timperio.crm.timperio_g1_4.repository.SaleRepository;

@Service
public class SalesMetricsServiceImpl {
    @Autowired
    private SaleRepository saleRepository;

    public SalesMetrics getSalesMetrics(boolean individual, Optional<Long> customerId)
            throws IllegalArgumentException, Exception {
        if (individual && !customerId.isPresent()) {
            throw new IllegalArgumentException("customerId must be provided if individual is true");
        } else if (!individual && customerId.isPresent()) {
            throw new IllegalArgumentException("customerId must not be provided if individual is false");
        }

        try {
            List<Sale> saleList;
            if (individual) {
                saleList = saleRepository.findByCustomer_CustomerId(customerId.get());
            } else {
                saleList = saleRepository.findAll();
            }

            int totalNumSales = saleList.size();
            BigDecimal totalSalesValue = BigDecimal.ZERO;
            BigDecimal averageSalesValue = BigDecimal.ZERO;
            HashMap<SaleType, Integer> salesByType = new HashMap<SaleType, Integer>();

            for (SaleType saleType : SaleType.values()) {
                salesByType.put(saleType, 0);
            }

            for (Sale sale : saleList) {
                totalSalesValue = totalSalesValue.add(sale.getDiscountedPrice() != null ? sale.getDiscountedPrice()
                        : sale.getOriginalPrice());
                salesByType.put(sale.getSaleType(), salesByType.get(sale.getSaleType()) + 1);
            }
            // Calculate average sales value
            if (totalNumSales > 0) {
                averageSalesValue = totalSalesValue.divide(BigDecimal.valueOf(totalNumSales), 2, RoundingMode.HALF_UP);
            }

            return new SalesMetrics(totalNumSales, totalSalesValue, averageSalesValue, salesByType);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error occured." + e.getMessage());
        }

    }
}
