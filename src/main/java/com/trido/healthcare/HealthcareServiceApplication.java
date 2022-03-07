package com.trido.healthcare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"com.trido.healthcare"})
public class HealthcareServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HealthcareServiceApplication.class, args);
    }

}
