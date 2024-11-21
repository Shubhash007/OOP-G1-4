package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.timperio.crm.timperio_g1_4.dto.ProductDto;
import com.example.timperio.crm.timperio_g1_4.entity.Product;
import com.example.timperio.crm.timperio_g1_4.repository.ProductRepository;
import com.example.timperio.crm.timperio_g1_4.service.ProductService;
import com.example.timperio.crm.timperio_g1_4.mapper.ProductMapper;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // Create a new product
    public ProductDto createProduct(ProductDto productDto) throws IllegalArgumentException, Exception {
        try {
            Product product = new Product();
            product.setProductName(productDto.getProductName());
            product.setProductDescription(productDto.getProductDescription());
            product.setProductVariant(productDto.getProductVariant());
            product.setProductPrice(productDto.getProductPrice());
            product = productRepository.save(product);
            return ProductMapper.maptoProductDto(product);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid product data submitted.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error occurred.");
        }
    }

    // Find all products
    public List<ProductDto> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return products.stream()
                    .map(ProductMapper::maptoProductDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An internal error occurred.");
        }
    }

    // Find a product by its ID
    public ProductDto getProductById(Long productId) {
        try {
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new NoSuchElementException("Product not found"));
            return ProductMapper.maptoProductDto(product);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Product not found");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An internal error occurred.");
        }
    }

    // search for products -- used for promotions
    public List<ProductDto> searchProducts(String name) {
        try {
            List<Product> products = productRepository.findByProductNameContaining(name);
            return products.stream()
                    .map(ProductMapper::maptoProductDto)
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid search query.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An internal error occurred.");
        }
    }
}
