package com.example.timperio.crm.timperio_g1_4.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerNewsletterDto;
import com.example.timperio.crm.timperio_g1_4.dto.NewsletterDto;
import com.example.timperio.crm.timperio_g1_4.dto.NewsletterTemplateDto;
import com.example.timperio.crm.timperio_g1_4.dto.ProcessNewsletterDto;
import com.example.timperio.crm.timperio_g1_4.dto.PromotionDto;
import com.example.timperio.crm.timperio_g1_4.entity.Customer;
import com.example.timperio.crm.timperio_g1_4.entity.CustomerNewsletter;
import com.example.timperio.crm.timperio_g1_4.entity.Newsletter;
import com.example.timperio.crm.timperio_g1_4.entity.NewsletterTemplate;
import com.example.timperio.crm.timperio_g1_4.entity.Promotion;
import com.example.timperio.crm.timperio_g1_4.repository.CustomerNewsletterRepository;
import com.example.timperio.crm.timperio_g1_4.repository.CustomerRepository;
import com.example.timperio.crm.timperio_g1_4.repository.NewsletterRepository;
import com.example.timperio.crm.timperio_g1_4.repository.NewsletterTemplateRepository;
import com.example.timperio.crm.timperio_g1_4.repository.ProductRepository;
import com.example.timperio.crm.timperio_g1_4.repository.PromotionRepository;
import com.example.timperio.crm.timperio_g1_4.service.NewsletterService;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class NewsletterServiceImpl implements NewsletterService {

    private static final Dotenv dotenv = Dotenv
            .load();

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    public NewsletterServiceImpl(JavaMailSender mailSender, SpringTemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Autowired
    private NewsletterRepository newsletterRepository;

    @Autowired
    private NewsletterTemplateRepository templateRepository;

    @Autowired
    private CustomerNewsletterRepository customerNewsletterRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PromotionRepository promotionRepository;

    @Autowired
    private ProductRepository productRepository;

    // ------------------ Newsletter Operations ------------------
    @Override
    public NewsletterDto createNewsletter(NewsletterDto newsletterDto) throws NoSuchElementException, Exception {
        try {
            NewsletterTemplate template = templateRepository.findById(newsletterDto.getTemplateId())
                    .orElseThrow(() -> new NoSuchElementException("Template not found"));

            Newsletter newsletter = new Newsletter();
            newsletter.setName(newsletterDto.getName());
            newsletter.setDescription(newsletterDto.getDescription());
            newsletter.setContent(newsletterDto.getContent());
            // newsletter.setCreatedAt(newsletterDto.getCreatedAt());
            newsletter.setNewsletterTemplate(template);

            Newsletter savedNewsletter = newsletterRepository.save(newsletter);
            return convertNewsletterToDto(savedNewsletter);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Template with inputted ID not found.");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error creating newsletter");
        }

    }

    @Override
    public List<NewsletterDto> getAllNewsletters() {
        try {
            return newsletterRepository.findAll().stream()
                    .map(this::convertNewsletterToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new NoSuchElementException("An internal error has occured.");
        }
    }

    @Override
    public NewsletterDto getNewsletterById(Long newsletterId) throws NoSuchElementException, Exception {
        try {
            Newsletter newsletter = newsletterRepository.findById(newsletterId)
                    .orElseThrow(() -> new NoSuchElementException("Newsletter not found"));
            return convertNewsletterToDto(newsletter);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Newsletter not found");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error has occured.");
        }

    }

    @Override
    public void deleteNewsletter(Long newsletterId) {
        newsletterRepository.deleteById(newsletterId);
    }

    // ------------------ Template Operations ------------------
    @Override
    public NewsletterTemplateDto createTemplate(NewsletterTemplateDto templateDto)
            throws IllegalArgumentException, Exception {
        try {
            NewsletterTemplate template = new NewsletterTemplate();
            template.setName(templateDto.getName());
            template.setDescription(templateDto.getDescription());
            template.setContent(templateDto.getContent());
            // template.setCreatedAt(templateDto.getCreatedAt());

            NewsletterTemplate savedTemplate = templateRepository.save(template);
            return convertTemplateToDto(savedTemplate);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid template data");
        } catch (Exception e) {
            throw new Exception("Error creating template");
        }
    }

    @Override
    public List<NewsletterTemplateDto> getAllTemplates() throws Exception {
        try {
            return templateRepository.findAll().stream()
                    .map(this::convertTemplateToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error has occured.");
        }
    }

    @Override
    public NewsletterTemplateDto getTemplateById(Long templateId) throws NoSuchElementException, Exception {
        try {
            NewsletterTemplate template = templateRepository.findById(templateId)
                    .orElseThrow(() -> new NoSuchElementException("Template not found"));
            return convertTemplateToDto(template);
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Template not found");
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("An internal error has occured.");
        }

    }

    @Override
    public void deleteTemplate(Long templateId) {
        templateRepository.deleteById(templateId);
    }

    // ------------------ CustomerNewsletter Operations ------------------
    @Override
    public CustomerNewsletterDto logCustomerNewsletter(CustomerNewsletterDto customerNewsletterDto) {
        Customer customer = customerRepository.findById(customerNewsletterDto.getCustomerId().longValue())
                .orElseThrow(() -> new NoSuchElementException("Customer not found"));

        Newsletter newsletter = newsletterRepository.findById(customerNewsletterDto.getNewsletterId().longValue())
                .orElseThrow(() -> new NoSuchElementException("Newsletter not found"));

        CustomerNewsletter customerNewsletter = new CustomerNewsletter();
        customerNewsletter.setCustomer(customer);
        customerNewsletter.setNewsletter(newsletter);
        customerNewsletter.setSendDate(customerNewsletterDto.getSendDate());
        customerNewsletter.setSendSuccess(customerNewsletterDto.isSendSuccess());

        CustomerNewsletter savedCustomerNewsletter = customerNewsletterRepository.save(customerNewsletter);
        return convertCustomerNewsletterToDto(savedCustomerNewsletter);
    }

    @Override
    public List<CustomerNewsletterDto> getAllCustomerNewsletters() {
        return customerNewsletterRepository.findAll().stream()
                .map(this::convertCustomerNewsletterToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerNewsletterDto> getNewslettersForCustomer(Long customerId) {
        return customerNewsletterRepository.findByCustomer_CustomerId(customerId).stream()
                .map(this::convertCustomerNewsletterToDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CustomerNewsletterDto> getCustomersForNewsletter(Long newsletterId) {
        return customerNewsletterRepository.findByNewsletter_Id(newsletterId).stream()
                .map(this::convertCustomerNewsletterToDto)
                .collect(Collectors.toList());
    }

    // ------------------ Newsletter Sending ------------------
    @Override
    public void sendNewsletter(ProcessNewsletterDto processNewsletterDto) throws Exception {
        // Placeholder for sending logic.
        // Customer customer =
        // customerRepository.findById(customerNewsletterDto.getCustomerId().longValue())
        // .orElseThrow(() -> new NoSuchElementException("Customer not found"));
        final List<Long> customers = processNewsletterDto.getCustomers();
        final Long newsletterTemplateId = processNewsletterDto.getNewsletterTemplate();
        final List<Long> promotionIds = processNewsletterDto.getPromotions();

        NewsletterTemplate newsletterTemplate = templateRepository.findById((long) newsletterTemplateId)
                .orElseThrow(() -> new NoSuchElementException("Newsletter template not found"));
        System.out.println("Sending newsletter to customer...");

        ArrayList<PromotionDto> promotions = new ArrayList<>();
        for (Long promotionId : promotionIds) {
            Promotion promotion = promotionRepository.findById((long) promotionId)
                    .orElseThrow(() -> new NoSuchElementException("Promotion not found"));
            promotions.add(mapToPromotionDto(promotion));
        }

        // // ------------------- Thymeleaf Template Engine -------------------
        // Context context = new Context();
        // context.setVariable("customerName", );
        // context.setVariable("promotions", promotions);

        // String templateContent = newsletterTemplate.getContent();
        // templateContent = templateContent.replace("\\${", "${");
        // // TemplateSpec templateSpec = new TemplateSpec(templateContent,
        // TemplateMode.HTML);

        // String htmlContent = templateEngine.process(templateContent, context);

        customerRepository.findAllById(customers).forEach(customer -> {

            if (customer.getEmail() != null) {
                try {
                    // ------------------- Thymeleaf Template Engine -------------------
                    Context context = new Context();
                    context.setVariable("customerName", "Bin Zhu");
                    context.setVariable("promotions", promotions);

                    String templateContent = newsletterTemplate.getContent();
                    templateContent = templateContent.replace("\\${", "${");
                    // TemplateSpec templateSpec = new TemplateSpec(templateContent,
                    // TemplateMode.HTML);

                    String htmlContent = templateEngine.process(templateContent, context);

                    MimeMessage mimeMessage = mailSender.createMimeMessage();
                    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

                    helper.setFrom(dotenv.get("MAIL_USERNAME"), "Timperio Newsletter");
                    helper.setTo(customer.getEmail());

                    helper.setSubject("Test");
                    helper.setText(htmlContent, true);

                    mailSender.send(mimeMessage);
                    // Implement actual sending logic here

                } catch (MessagingException | UnsupportedEncodingException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        });
        // ------------------- Send Email -------------------
    }

    // ------------------ Helper Converters ------------------
    private NewsletterDto convertNewsletterToDto(Newsletter newsletter) {
        NewsletterDto dto = new NewsletterDto();
        dto.setId(newsletter.getId());
        dto.setName(newsletter.getName());
        dto.setDescription(newsletter.getDescription());
        dto.setContent(newsletter.getContent());
        dto.setCreatedAt(newsletter.getCreatedAt());
        if (newsletter.getNewsletterTemplate() != null) {
            dto.setTemplateId(newsletter.getNewsletterTemplate().getId());
        }
        return dto;
    }

    private NewsletterTemplateDto convertTemplateToDto(NewsletterTemplate template) {
        NewsletterTemplateDto dto = new NewsletterTemplateDto();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setContent(template.getContent());
        dto.setCreatedAt(template.getCreatedAt());
        return dto;
    }

    private CustomerNewsletterDto convertCustomerNewsletterToDto(CustomerNewsletter customerNewsletter) {
        CustomerNewsletterDto dto = new CustomerNewsletterDto();
        dto.setId(customerNewsletter.getId());
        dto.setSendDate(customerNewsletter.getSendDate());
        dto.setSendSuccess(customerNewsletter.isSendSuccess());
        if (customerNewsletter.getCustomer() != null) {
            dto.setCustomerId(customerNewsletter.getCustomer().getCustomerId().longValue());
        }
        if (customerNewsletter.getNewsletter() != null) {
            dto.setNewsletterId(customerNewsletter.getNewsletter().getId());
        }
        return dto;
    }

    private PromotionDto mapToPromotionDto(Promotion promotion) {
        PromotionDto promotionDto = new PromotionDto();
        promotionDto.setPromotionId(promotion.getPromotionId());
        promotionDto.setPromotionName(promotion.getPromotionName());
        promotionDto.setPromotionDescription(promotion.getPromotionDescription());
        promotionDto.setPromotionType(promotion.getPromotionType());
        promotionDto.setValidUntil(promotion.getValidUntil());
        promotionDto.setDiscountRate(promotion.getDiscountRate());
        promotionDto.setFreeQuantity(promotion.getFreeQuantity());
        promotionDto.setBuyQuantity(promotion.getBuyQuantity());
        promotionDto.setFrequentShopperRequired(promotion.isFrequentShopperRequired());

        // Set main product id
        if (promotion.getMainProduct() != null) {
            promotionDto.setMainProductId(promotion.getMainProduct().getProductId());
        }

        // Set related product ids
        if (promotion.getRelatedProducts() != null) {
            promotionDto.setRelatedProductIds(
                    promotion.getRelatedProducts().stream()
                            .map(product -> product.getProductId())
                            .collect(Collectors.toList()));
        }

        return promotionDto;
    }

}
