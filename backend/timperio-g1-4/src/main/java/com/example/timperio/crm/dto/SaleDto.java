package com.example.timperio.crm.dto;

import java.util.Date;

import com.example.timperio.crm.entity.Customer;
import com.example.timperio.crm.entity.Product;
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
    private String saleType;
    private String digitalChannel;
    private String shippingMethod;
    private Long quantity;
    private double productPrice;
    private Customer customer;
    private Product product;
}
