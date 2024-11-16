package com.example.timperio.crm.timperio_g1_4.dto;

import java.math.BigDecimal;
import java.sql.Date;

import com.example.timperio.crm.timperio_g1_4.entity.Customer;
import com.example.timperio.crm.timperio_g1_4.entity.Product;
import com.example.timperio.crm.timperio_g1_4.entity.Promotion;
import com.example.timperio.crm.timperio_g1_4.enums.SaleType;
import com.example.timperio.crm.timperio_g1_4.enums.ShippingMethod;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDto {
    private Long saleId;
    private Date saleDate;
    private SaleType saleType;
    private String digital;
    private ShippingMethod shippingMethod;
    private Long quantity;
    private BigDecimal originalPrice;
    private BigDecimal discountedPrice;
    private Customer customer;
    private Product product;
    private Promotion promotion;
}
