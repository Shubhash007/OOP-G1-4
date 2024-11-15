package com.example.timperio.crm.timperio_g1_4.mapper;

import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
import com.example.timperio.crm.timperio_g1_4.entity.Sale;

public class SaleMapper {

    public static SaleDto mapToSaleDto(Sale sale) {
        return new SaleDto(
                sale.getSaleId(),
                sale.getSaleDate(),
                sale.getSaleType(),
                sale.getDigital(),
                sale.getShippingMethod(),
                sale.getQuantity(),
                sale.getOriginalPrice(),
                sale.getDiscountedPrice(),
                sale.getCustomer(),
                sale.getProduct(),
                sale.getPromotion());
    }

    public static Sale mapToSale(SaleDto saleDto) {
        return new Sale(
                saleDto.getSaleId(),
                saleDto.getSaleDate(),
                saleDto.getSaleType(),
                saleDto.getShippingMethod(),
                saleDto.getDigital(),
                saleDto.getQuantity(),
                saleDto.getOriginalPrice(),
                saleDto.getDiscountedPrice(),
                saleDto.getCustomer(),
                saleDto.getProduct(),
                saleDto.getPromotion());
    }

}
