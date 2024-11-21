package com.example.timperio.crm.timperio_g1_4.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.ProductDto;

@Service
public interface ProductService {

    ProductDto createProduct(ProductDto productDto) throws IllegalArgumentException, Exception;

    List<ProductDto> getAllProducts() throws Exception;

    ProductDto getProductById(Long productId) throws NoSuchElementException, Exception;

    List<ProductDto> searchProducts(String name) throws IllegalArgumentException, Exception;
}