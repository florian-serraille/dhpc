package com.devlabs.dhpc.billingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

@EnableFeignClients
@SpringBootApplication
public class BillingServiceApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(BillingServiceApplication.class, args);
	}
	
}
