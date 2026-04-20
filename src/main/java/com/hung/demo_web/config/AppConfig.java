package com.hung.demo_web.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration // Báo cho Spring Boot biết đây là file cấu hình hệ thống
@EnableScheduling // Bật tính năng chạy Job định kỳ (Dùng cho thư mục job)
@EnableAsync      // Bật tính năng chạy ngầm đa luồng (Dùng cho thư mục mail)
public class AppConfig {
}