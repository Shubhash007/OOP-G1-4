package com.example.timperio.crm.timperio_g1_4.service;

import java.util.List;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerNewsletterDto;
import com.example.timperio.crm.timperio_g1_4.dto.NewsletterDto;
import com.example.timperio.crm.timperio_g1_4.dto.NewsletterTemplateDto;

public interface NewsletterService {
    NewsletterDto createNewsletter(NewsletterDto newsletterDto);
    List<NewsletterDto> getAllNewsletters();
    NewsletterDto getNewsletterById(Long newsletterId);
    void deleteNewsletter(Long newsletterId);

    NewsletterTemplateDto createTemplate(NewsletterTemplateDto templateDto);
    List<NewsletterTemplateDto> getAllTemplates();
    NewsletterTemplateDto getTemplateById(Long templateId);
    void deleteTemplate(Long templateId);

    CustomerNewsletterDto logCustomerNewsletter(CustomerNewsletterDto customerNewsletterDto);
    List<CustomerNewsletterDto> getAllCustomerNewsletters();
    List<CustomerNewsletterDto> getNewslettersForCustomer(Long customerId);
    List<CustomerNewsletterDto> getCustomersForNewsletter(Long newsletterId);

    void sendNewsletter();
}
