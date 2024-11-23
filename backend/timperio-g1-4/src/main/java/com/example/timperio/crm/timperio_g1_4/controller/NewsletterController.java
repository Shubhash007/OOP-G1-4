package com.example.timperio.crm.timperio_g1_4.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.timperio.crm.timperio_g1_4.dto.CustomerNewsletterDto;
import com.example.timperio.crm.timperio_g1_4.dto.NewsletterDto;
import com.example.timperio.crm.timperio_g1_4.dto.NewsletterTemplateDto;
import com.example.timperio.crm.timperio_g1_4.dto.ProcessNewsletterDto;
import com.example.timperio.crm.timperio_g1_4.service.NewsletterService;

@RestController
@RequestMapping("/newsletters")
public class NewsletterController {

    private final NewsletterService newsletterService;

    public NewsletterController(NewsletterService newsletterService) {
        this.newsletterService = newsletterService;
    }

    @PostMapping("/create-template")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createTemplate(@RequestBody NewsletterTemplateDto templateDto)
            throws IllegalArgumentException, Exception {
        try {
            NewsletterTemplateDto createdTemplate = newsletterService.createTemplate(templateDto);
            return new ResponseEntity<>(createdTemplate, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all-templates")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllTemplates() {
        try {
            return new ResponseEntity<>(newsletterService.getAllTemplates(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-template/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getTemplateById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(newsletterService.getTemplateById(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Template with inputted ID not found.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_MARKETING')")
    public ResponseEntity<?> createNewsletter(@RequestBody NewsletterDto newsletterDto) {
        try {
            return new ResponseEntity<>(newsletterService.createNewsletter(newsletterDto), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get-all")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllNewsletters() {
        try {
            return new ResponseEntity<>(newsletterService.getAllNewsletters(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{id:\\d+}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getNewsletterById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(newsletterService.getNewsletterById(id), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>("Newsletter with inputted ID not found.", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send")
    @PreAuthorize("hasRole('ROLE_MARKETING')")
    public ResponseEntity<String> sendNewsletter(@RequestBody ProcessNewsletterDto processNewsletterDto) {
        try {
            newsletterService.sendNewsletter(processNewsletterDto);
            return ResponseEntity.ok("Newsletter sent successfully to" + processNewsletterDto.getCustomers().toString() );
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error sending newsletter: " + e.getMessage());
        }

    }
}
