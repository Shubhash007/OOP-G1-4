package com.example.timperio.crm.timperio_g1_4.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<NewsletterTemplateDto> createTemplate(@RequestBody NewsletterTemplateDto templateDto) {
        NewsletterTemplateDto createdTemplate = newsletterService.createTemplate(templateDto);
        return ResponseEntity.ok(createdTemplate);
    }

    @GetMapping("/get-all-templates")
    public ResponseEntity<List<NewsletterTemplateDto>> getAllTemplates() {
        return ResponseEntity.ok(newsletterService.getAllTemplates());
    }

    @GetMapping("/get-template/{id}")
    public ResponseEntity<NewsletterTemplateDto> getTemplateById(@PathVariable Long id) {
        return ResponseEntity.ok(newsletterService.getTemplateById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<NewsletterDto> createNewsletter(@RequestBody NewsletterDto newsletterDto) {
        return ResponseEntity.ok(newsletterService.createNewsletter(newsletterDto));
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<NewsletterDto>> getAllNewsletters() {
        return ResponseEntity.ok(newsletterService.getAllNewsletters());
    }

    @GetMapping("/get/{id:\\d+}")
    public ResponseEntity<NewsletterDto> getNewsletterById(@PathVariable Long id) {
        return ResponseEntity.ok(newsletterService.getNewsletterById(id));
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendNewsletter(@RequestBody ProcessNewsletterDto processNewsletterDto) {
        newsletterService.sendNewsletter(processNewsletterDto);
        return ResponseEntity.ok("Newsletter sent successfully");
    }
}
