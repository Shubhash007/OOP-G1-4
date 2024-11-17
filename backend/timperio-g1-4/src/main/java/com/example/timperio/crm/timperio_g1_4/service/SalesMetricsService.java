package com.example.timperio.crm.timperio_g1_4.service;

import com.example.timperio.crm.timperio_g1_4.entity.SalesMetrics;

public interface SalesMetricsService {
    SalesMetrics getSalesMetrics(boolean individual, Long customerId);
}
