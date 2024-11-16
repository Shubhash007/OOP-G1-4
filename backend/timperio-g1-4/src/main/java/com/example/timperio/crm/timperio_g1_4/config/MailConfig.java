package com.example.timperio.crm.timperio_g1_4.config;

import org.springframework.context.annotation.Configuration;

import io.github.cdimascio.dotenv.Dotenv;

@Configuration
public class MailConfig {

    private static final Dotenv dotenv = Dotenv.load();

    public MailConfig() {
        System.setProperty("spring.mail.username", getEnv("MAIL_USERNAME"));
        System.setProperty("spring.mail.password", getEnv("MAIL_PASSWORD"));
    }

    private static String getEnv(String key) {
        return dotenv.get(key);
    }
}
