// package com.example.timperio.crm.timperio_g1_4.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// import com.example.timperio.crm.timperio_g1_4.dto.SaleDto;
// import com.example.timperio.crm.timperio_g1_4.service.SaleService;
// import org.springframework.web.bind.annotation.RequestBody;


// @RestController
// @RequestMapping("/sale")
// public class SaleController {

//     @Autowired
//     private SaleService saleService;

//     @RequestMapping("/create")
//     public ResponseEntity<SaleDto> createProduct(@RequestBody SaleDto saleDto){
//         SaleDto savedProduct = saleService.createProduct(saleDto);
//         return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
//     }
    


// }
