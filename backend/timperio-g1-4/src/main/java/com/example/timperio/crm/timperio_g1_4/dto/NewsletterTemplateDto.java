package com.example.timperio.crm.timperio_g1_4.dto;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewsletterTemplateDto {
    private Long id;
    private String name;
    private String description;
    private String content;
    private LocalDate createdAt;
}
