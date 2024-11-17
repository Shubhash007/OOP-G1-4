package com.example.timperio.crm.timperio_g1_4.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashMap;

import com.example.timperio.crm.timperio_g1_4.enums.SaleType;

@Data
@AllArgsConstructor
public class SalesMetrics {
    private int totalNumSales;
    private BigDecimal totalSalesValue;
    private BigDecimal averageSalesValue;
    private HashMap<SaleType, Integer> salesByType;
}
