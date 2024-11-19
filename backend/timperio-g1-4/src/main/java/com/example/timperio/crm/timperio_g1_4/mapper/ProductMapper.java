package com.example.timperio.crm.timperio_g1_4.mapper;

import com.example.timperio.crm.timperio_g1_4.dto.ProductDto;
import com.example.timperio.crm.timperio_g1_4.entity.Product;

public class ProductMapper {

    public static ProductDto maptoProductDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setProductDescription(product.getProductDescription());
        productDto.setProductVariant(product.getProductVariant());
        productDto.setProductPrice(product.getProductPrice());
        return productDto;
    }

    public static Product maptoProduct(ProductDto productDto) {
        Product product = new Product();
        product.setProductId(productDto.getProductId());
        product.setProductName(productDto.getProductName());
        product.setProductDescription(productDto.getProductDescription());
        product.setProductVariant(productDto.getProductVariant());
        product.setProductPrice(productDto.getProductPrice());
        return product;
    }
}
