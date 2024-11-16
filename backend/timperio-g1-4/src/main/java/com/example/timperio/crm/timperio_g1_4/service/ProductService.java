package com.example.timperio.crm.timperio_g1_4.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.ProductDto;
import com.example.timperio.crm.timperio_g1_4.entity.Product;
import com.example.timperio.crm.timperio_g1_4.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create a new product
    public ProductDto createProduct(ProductDto productDto) {
        Product product = new Product();
        product.setProductName(productDto.getProductName());
        product.setProductDescription(productDto.getProductDescription());
        product.setProductVariant(productDto.getProductVariant());
        product.setProductPrice(productDto.getProductPrice());
        product = productRepository.save(product);
        return convertToDto(product);
    }

    // Find all products
    public List<ProductDto> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // Find a product by its ID
    public ProductDto getProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return convertToDto(product);
    }

    // Convert entity to DTO
    private ProductDto convertToDto(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setProductId(product.getProductId());
        productDto.setProductName(product.getProductName());
        productDto.setProductDescription(product.getProductDescription());
        productDto.setProductVariant(product.getProductVariant());
        productDto.setProductPrice(product.getProductPrice());
        return productDto;
    }

    //search for products -- used for promotions
    public List<ProductDto> searchProducts(String name) {
        List<Product> products = productRepository.findByProductNameContaining(name);
        return products.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
}
