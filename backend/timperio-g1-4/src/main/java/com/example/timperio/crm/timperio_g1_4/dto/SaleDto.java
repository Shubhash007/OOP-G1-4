package com.example.timperio.crm.timperio_g1_4.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SaleDto{

    private Long rowNo;
    private Date saleDate;
    private String saleType;
    private String digital;
    private Long customerId;
    private Long zipCode;
    private String shippingMethod;
    private String product;
    private String variant;
    private Long quantity;
    private double price;
    private double productPrice;
}
