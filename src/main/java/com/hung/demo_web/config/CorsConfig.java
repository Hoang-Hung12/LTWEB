package com.hung.demo_web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**") // Áp dụng luật này cho TẤT CẢ các API (Bắt đầu bằng /api/...)
                        .allowedOrigins("*") // Cho phép tất cả các domain (localhost:5500, localhost:3000...) gọi vào
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Mở khóa các hành động
                        .allowedHeaders("*"); // Chấp nhận mọi loại Header
            }
        };
    }
}