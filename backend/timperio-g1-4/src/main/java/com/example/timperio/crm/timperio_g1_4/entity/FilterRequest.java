package com.example.timperio.crm.timperio_g1_4.entity;

import java.util.Optional;

import com.example.timperio.crm.timperio_g1_4.enums.SaleType;

import lombok.Data;

@Data
public class FilterRequest {
    private Optional<Long> customerId;
    private Optional<SaleType> saleType;
    private Optional<Double> minValue;
    private Optional<Double> maxValue;
    private Optional<String> startDate;
    private Optional<String> endDate;

    public FilterRequest(Optional<Long> customerId, Optional<SaleType> saleType, Optional<Double> minValue,
            Optional<Double> maxValue, Optional<String> startDate, Optional<String> endDate) {
        this.customerId = customerId;
        this.saleType = saleType;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
