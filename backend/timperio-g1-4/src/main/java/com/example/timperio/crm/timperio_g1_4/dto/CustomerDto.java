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
    private LocalDate lastPurchaseDate;
    private Boolean acceptNewsletter;
    private String email;
    private Boolean returningCustomer;
    private Integer purchaseCount;
    private BigDecimal totalExpenditure;

}
