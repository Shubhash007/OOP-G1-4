package com.example.timperio.crm.timperio_g1_4.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.dto.ProductDto;
import com.example.timperio.crm.timperio_g1_4.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Create a new product
    @PostMapping("/create")
    @PreAuthorize("isAuthenticated()")
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDto createProduct(@RequestBody ProductDto productDto) {
        return productService.createProduct(productDto);
    }

    // Get all products
    @GetMapping("/get-all")
    @PreAuthorize("isAuthenticated()")
    public List<ProductDto> getAllProducts() {
        return productService.getAllProducts();
    }

    // Get a product by ID
    @GetMapping("/get/{id}")
    @PreAuthorize("isAuthenticated()")
    public ProductDto getProductById(@PathVariable("id") Long productId) {
        return productService.getProductById(productId);
    }

    // Search products by name
    @GetMapping("/search")
    @PreAuthorize("isAuthenticated()")
    public List<ProductDto> searchProducts(@RequestParam String name) {
        return productService.searchProducts(name);
    }
}
