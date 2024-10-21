package com.example.timperio.crm.mapper;

import com.example.timperio.crm.dto.SaleDto;
import com.example.timperio.crm.entity.Sale;

public class SaleMapper {

    public static SaleDto mapToSaleDto(Sale sale) {
        return new SaleDto(
                sale.getSaleId(),
                sale.getSaleDate(),
                sale.getSaleType(),
                sale.getDigitalChannel(),
                sale.getShippingMethod(),
                sale.getQuantity(),
                sale.getProductPrice(),
                sale.getCustomer(),
                sale.getProduct());
    }

    public static Sale mapToSale(SaleDto saleDto) {
        return new Sale(
                saleDto.getSaleId(),
                saleDto.getSaleDate(),
                saleDto.getSaleType(),
                saleDto.getDigitalChannel(),
                saleDto.getShippingMethod(),
                saleDto.getQuantity(),
                saleDto.getProductPrice(),
                saleDto.getCustomer(),
                saleDto.getProduct());
    }

}
