package com.example.spring_beans_demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
    // Оставьте только специальные бины
    @Bean
    public String appName() {
        return "Spring Java Config Demo";
    }

}