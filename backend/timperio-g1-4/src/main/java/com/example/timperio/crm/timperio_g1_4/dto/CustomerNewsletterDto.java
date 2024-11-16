package com.example.timperio.crm.timperio_g1_4.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerNewsletterDto {
    private Long id;
    private Long customerId;
    private Long newsletterId;
    private boolean sendSuccess;
    private LocalDate sendDate;
}
