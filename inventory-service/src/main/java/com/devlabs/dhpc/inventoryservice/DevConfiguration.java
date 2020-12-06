package com.devlabs.dhpc.inventoryservice;

import com.devlabs.dhpc.inventoryservice.inventory.Product;
import com.devlabs.dhpc.inventoryservice.inventory.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.math.BigDecimal;
import java.util.Arrays;

@Profile("dev")
@Configuration
public class DevConfiguration {
	
	@Bean
	CommandLineRunner start(final ProductRepository productRepository) {
		
		return args -> productRepository.saveAll(
				Arrays.asList(new Product(1L, "Dell XPS 13", BigDecimal.valueOf(1199.55)),
				              new Product(2L, "Asus ZenBook 13", BigDecimal.valueOf(999.99)),
				              new Product(3L, "MacBook Air 2020", BigDecimal.valueOf(1249.00))));
	}
}
