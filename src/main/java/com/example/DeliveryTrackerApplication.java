package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // ★ JPA Auditing을 활성화합니다.
@SpringBootApplication
public class DeliveryTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliveryTrackerApplication.class, args);
    }

}