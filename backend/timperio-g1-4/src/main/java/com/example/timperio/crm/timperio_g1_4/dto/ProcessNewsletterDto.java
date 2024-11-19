package com.example.timperio.crm.timperio_g1_4.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessNewsletterDto {
    private List<Long> customers;
    private Long newsletterTemplate;
    private List<Long> promotions;
}


