package com.example.timperio.crm.timperio_g1_4.mapper;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.entity.Sale;

public class SaleMapper {

    public static SaleDto mapToSaleDto(Sale sale){
        return new SaleDto(
            sale.getRowNo(),
            sale.getSaleDate(),
            sale.getSaleType(),
            sale.getDigital(),
            sale.getCustomerId(),
            sale.getZipCode(),
            sale.getShippingMethod(),
            sale.getProduct(),
            sale.getVariant(),
            sale.getQuantity(),
            sale.getPrice(),
            sale.getProductPrice()
        );        
    }

    public static Sale mapToSale(SaleDto saleDto){
        return new Sale(
            saleDto.getRowNo(),
            saleDto.getSaleDate(),
            saleDto.getSaleType(),
            saleDto.getDigital(),
            saleDto.getCustomerId(),
            saleDto.getZipCode(),
            saleDto.getShippingMethod(),
            saleDto.getProduct(),
            saleDto.getVariant(),
            saleDto.getQuantity(),
            saleDto.getPrice(),
            saleDto.getProductPrice()
        );        
    }


}
