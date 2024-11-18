package com.example.timperio.crm.timperio_g1_4.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long customerId;
    private List<Long> zipCode;
    private LocalDate lastPurchaseDate = null;
    private Boolean acceptNewsletter;
    private String email;
    private Boolean returningCustomer = false;
    private Integer purchaseCount = 0;
    private BigDecimal totalExpenditure = BigDecimal.ZERO;

    // partial constructor for CustomerDto for creating new customer
    public CustomerDto(List<Long> zipCode, Boolean acceptNewsletter, String email) {
        this.zipCode = zipCode;
        this.acceptNewsletter = acceptNewsletter;
        this.email = email;
    }
}
